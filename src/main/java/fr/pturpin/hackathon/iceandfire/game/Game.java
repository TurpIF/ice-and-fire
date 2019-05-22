package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Game implements GameRepository {

    private int playerGold;
    private CellType[] grid;
    private Map<Position, PlayerUnit> playerUnits = new HashMap<>();
    private Map<Position, OpponentUnit> opponentUnits = new HashMap<>();
    private Map<Position, PlayerBuilding> playerBuildings = new HashMap<>();
    private Map<Position, OpponentBuilding> opponentBuildings = new HashMap<>();

    public GameInitialization onInitialization() {
        return new OnInitialization();
    }

    public GameNewTurn onNewTurn() {
        return new OnNewTurn();
    }

    public GameStrategy asStrategy() {
        return Collections::emptyList;
    }

    @Override
    public int getPlayerGold() {
        return playerGold;
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
    public CellType getCellType(Position position) {
        int index = position.getY() * 12 + position.getX();
        return grid[index];
    }

    @Override
    public GameCell getCell(Position position) {
        return new GameCell(this, position);
    }

    private class OnNewTurn implements GameNewTurn {

        @Override
        public void setPlayerGold(int playerGold) {
            Game.this.playerGold = playerGold;
        }

        @Override
        public void setPlayerRevenue(int playerRevenue) {
            // TODO
        }

        @Override
        public void setOpponentGold(int opponentGold) {
            // TODO
        }

        @Override
        public void setOpponentRevenue(int opponentRevenue) {
            // TODO
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
                OpponentBuilding opponentBuilding = new OpponentBuilding(buildingType);
                Game.this.opponentBuildings.put(position, opponentBuilding);
            } else {
                throwUnsupportedOwner(owner);
            }
        }

        @Override
        public void addUnit(Owner owner, int unitId, int level, Position position) {
            if (owner == Owner.ME) {
                PlayerUnit playerUnit = new PlayerUnit(unitId, position, new TrainedPlayerUnit(level));
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
            // TODO
        }

        @Override
        public void addMineSpot(Position position) {
            // TODO
        }
    }
}
