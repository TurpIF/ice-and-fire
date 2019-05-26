package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.strategy.GameStrategy;
import fr.pturpin.hackathon.iceandfire.strategy.GameStrategyImpl;
import fr.pturpin.hackathon.iceandfire.strategy.graph.PositionDfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.TraversalVisitor;
import fr.pturpin.hackathon.iceandfire.unit.*;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game implements GameRepository {

    private int playerGold;
    private CellType[] grid;
    private OpponentBuilding opponentQg;
    private Map<Position, PlayerUnit> playerUnits = new HashMap<>();
    private Map<Position, OpponentUnit> opponentUnits = new HashMap<>();
    private Map<Position, PlayerBuilding> playerBuildings = new HashMap<>();
    private Map<Position, OpponentBuilding> opponentBuildings = new HashMap<>();
    private Set<Position> mineSpots = new HashSet<>();

    private final PositionDfsTraversal traversal;
    private final PropagateActiveTerritoryVisitor activatingVisitor;
    private final PropagateDeactivateTerritoryVisitor deactivatingVisitor;

    public Game() {
        traversal = new PositionDfsTraversal();
        activatingVisitor = new PropagateActiveTerritoryVisitor();
        deactivatingVisitor = new PropagateDeactivateTerritoryVisitor();
    }

    public GameInitialization onInitialization() {
        return new OnInitialization();
    }

    public GameNewTurn onNewTurn() {
        return new OnNewTurn();
    }

    public GameStrategy asStrategy() {
        return new GameStrategyImpl(this);
    }

    @Override
    public int getPlayerGold() {
        return playerGold;
    }

    @Override
    public Stream<PlayerUnit> getAllPlayerUnits() {
        return playerUnits.values().stream();
    }

    @Override
    public Optional<PlayerUnit> getPlayerUnitAt(Position position) {
        return Optional.ofNullable(playerUnits.get(position));
    }

    @Override
    public Optional<PlayerBuilding> getPlayerBuildingAt(Position position) {
        return Optional.ofNullable(playerBuildings.get(position));
    }

    @Override
    public Optional<OpponentUnit> getOpponentUnitAt(Position position) {
        return Optional.ofNullable(opponentUnits.get(position));
    }

    @Override
    public Optional<OpponentBuilding> getOpponentBuildingAt(Position position) {
        return Optional.ofNullable(opponentBuildings.get(position));
    }

    @Override
    public OpponentBuilding getOpponentQg() {
        return opponentQg;
    }

    @Override
    public CellType getCellType(Position position) {
        int index = toIndex(position);
        return grid[index];
    }

    @Override
    public GameCell getCell(Position position) {
        return new GameCell(this, position);
    }

    @Override
    public Stream<GameCell> getAllCells() {
        return getAllPositions().map(this::getCell);
    }

    private Stream<Position> getAllPositions() {
        return IntStream.range(0, 12).boxed().flatMap(x ->
                IntStream.range(0, 12).mapToObj(y -> new Position(x, y)));
    }

    @Override
    public boolean isMineSpot(Position position) {
        return mineSpots.contains(position);
    }

    @Override
    public int getPlayerMineCount() {
        return (int) playerBuildings.values().stream()
                .filter(building -> building.getType() == BuildingType.MINE)
                .count();
    }

    @Override
    public void moveUnit(PlayerUnit playerUnit, GameCell newCell) {
        Position oldPosition = playerUnit.getPosition();
        Position newPosition = newCell.getPosition();

        propagateActiveTerritory(newPosition);

        playerUnits.remove(oldPosition);
        playerUnits.put(newPosition, playerUnit);

        opponentBuildings.remove(newPosition);
        opponentUnits.remove(newPosition);
    }

    @Override
    public void invokeNewUnit(TrainedUnit trainedUnit, GameCell cell) {
        PlayerUnit playerUnit = new PlayerUnit(-1, cell, trainedUnit, false);
        moveUnit(playerUnit, cell);
        playerGold -= trainedUnit.getTrainingCost();
    }

    private void propagateActiveTerritory(Position origin) {
        activatingVisitor.setOrigin(origin);
        traversal.traverse(origin, activatingVisitor);

        for (Position neighbor : origin.getNeighbors()) {
            traversal.traverse(neighbor, deactivatingVisitor);
            deactivatingVisitor.apply();
        }
    }

    @Override
    public void invokeNewBuilding(TrainedPlayerBuilding trainedPlayerBuilding, GameCell cell) {
        onNewTurn().addBuilding(Owner.ME, trainedPlayerBuilding.getBuildingType(), cell.getPosition());
        playerGold -= trainedPlayerBuilding.getCost();
    }

    private class PropagateDeactivateTerritoryVisitor implements TraversalVisitor<Position> {

        private boolean isIsolated = true;
        private List<Integer> toDeactivate = new ArrayList<>();

        @Override
        public TraversalContinuation visit(Position element) {
            if (isOpponentQg(element)) {
                isIsolated = false;
                return TraversalContinuation.STOP;
            }

            int index = toIndex(element);

            if (grid[index] != CellType.ACTIVE_THEIR) {
                return TraversalContinuation.SKIP;
            }

            toDeactivate.add(index);
            return TraversalContinuation.CONTINUE;
        }

        private boolean isOpponentQg(Position element) {
            OpponentBuilding opponentQg = getOpponentQg();
            return opponentQg != null && opponentQg.getPosition().equals(element);
        }

        void apply() {
            if (!isIsolated) {
                init();
                return;
            }

            toDeactivate.forEach(index -> {
                grid[index] = CellType.INACTIVE_THEIR;
            });

            init();
        }

        private void init() {
            toDeactivate.clear();
            isIsolated = true;
        }
    }

    private class PropagateActiveTerritoryVisitor implements TraversalVisitor<Position> {


        private Position origin;

        void setOrigin(Position origin) {
            this.origin = origin;
        }
        @Override
        public TraversalContinuation visit(Position element) {
            int index = toIndex(element);

            if (grid[index] != CellType.INACTIVE_MINE && !element.equals(origin)) {
                return TraversalContinuation.SKIP;
            }

            grid[index] = CellType.ACTIVE_MINE;
            return TraversalContinuation.CONTINUE;
        }

    }

    private int toIndex(Position position) {
        return position.getY() * 12 + position.getX();
    }

    private class OnNewTurn implements GameNewTurn {

        @Override
        public void setPlayerGold(int playerGold) {
            Game.this.playerGold = playerGold;
        }

        @Override
        public void setPlayerRevenue(int playerRevenue) {
        }

        @Override
        public void setOpponentGold(int opponentGold) {
        }

        @Override
        public void setOpponentRevenue(int opponentRevenue) {
        }

        @Override
        public void setGrid(CellType[] grid) {
            Game.this.grid = grid;
        }

        @Override
        public void setBuildingCount(int buildingCount) {
            Game.this.playerBuildings.clear();
            Game.this.opponentBuildings.clear();
        }

        @Override
        public void setUnitCount(int unitCount) {
            Game.this.playerUnits.clear();
            Game.this.opponentUnits.clear();
        }

        @Override
        public void addBuilding(Owner owner, BuildingType buildingType, Position position) {
            if (owner == Owner.ME) {
                PlayerBuilding playerBuilding = new PlayerBuilding(buildingType);
                Game.this.playerBuildings.put(position, playerBuilding);
            } else if (owner == Owner.OTHER) {
                OpponentBuilding opponentBuilding = new OpponentBuilding(buildingType, position);
                Game.this.opponentBuildings.put(position, opponentBuilding);

                if (buildingType == BuildingType.QG) {
                    Game.this.opponentQg = opponentBuilding;
                }
            } else {
                throwUnsupportedOwner(owner);
            }
        }

        @Override
        public void addUnit(Owner owner, int unitId, int level, Position position) {
            if (owner == Owner.ME) {
                PlayerUnit playerUnit = new PlayerUnit(unitId, getCell(position), new TrainedUnit(level));
                Game.this.playerUnits.put(position, playerUnit);
            } else if (owner == Owner.OTHER) {
                OpponentUnit opponentUnit = new OpponentUnit(level);
                Game.this.opponentUnits.put(position, opponentUnit);
            } else {
                throwUnsupportedOwner(owner);
            }
        }

        private void throwUnsupportedOwner(Owner owner) {
            throw new UnsupportedOperationException("Unknown owner type: " + owner);
        }

    }

    private class OnInitialization implements GameInitialization {

        @Override
        public void setMineSpotCount(int mineSpotCount) {
        }

        @Override
        public void addMineSpot(Position position) {
            mineSpots.add(position);
        }
    }
}
