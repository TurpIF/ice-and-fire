package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.graph.DfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.PositionDfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.TraversalVisitor;
import fr.pturpin.hackathon.iceandfire.unit.*;

import java.util.Comparator;
import java.util.Optional;

public class DefensiveMoveComparator implements Comparator<MoveCommand> {

    private final DfsTraversal<Position> traversal;
    private final KillCountVisitor visitor;
    private final GameRepository gameRepository;

    public DefensiveMoveComparator(GameRepository gameRepository) {
        traversal = new PositionDfsTraversal();
        visitor = new KillCountVisitor(gameRepository);
        this.gameRepository = gameRepository;
    }

    @Override
    public int compare(MoveCommand o1, MoveCommand o2) {
        return Comparator.comparingInt(this::evaluate).compare(o1, o2);
    }

    private int evaluate(MoveCommand command) {
        GameCell newCell = command.getCell();
        Position oldPosition = command.getPlayerUnit().getPosition();
        Position newPosition = newCell.getPosition();
        boolean hasMoved = !oldPosition.equals(newPosition);

        if (hasMoved) {
            GameCell oldCell = gameRepository.getCell(oldPosition);
            PlayerCount count = getBeatablePlayerCount(oldCell, newCell);
            return count.score();
        }

        return PlayerCount.noMoveScore();
    }

    private PlayerCount getBeatablePlayerCount(GameCell oldCell, GameCell newCell) {
        PlayerCount count = new PlayerCount();

        Position position = oldCell.getPosition();
        Position newPosition = newCell.getPosition();
        for (Position neighbor : position.getNeighbors()) {
            GameCell neighborCell = gameRepository.getCell(neighbor);
            if (neighborCell.isInMyTerritory()) {
                count.add(getKillCountFrom(neighborCell.getPosition(), position, newPosition));
            }
        }

        return count;
    }

    private PlayerCount getKillCountFrom(Position newVisit, Position origin, Position newPosition) {
        visitor.clear();
        visitor.setOrigin(origin);
        visitor.setNewPosition(newPosition);
        traversal.traverse(newVisit, visitor);
        return visitor.getCount();
    }

    private class KillCountVisitor implements TraversalVisitor<Position> {

        private final GameRepository gameRepository;

        private PlayerCount count = new PlayerCount();
        private Position origin;
        private Position newPosition;

        private KillCountVisitor(GameRepository gameRepository) {
            this.gameRepository = gameRepository;
        }

        void setOrigin(Position origin) {
            this.origin = origin;
        }

        void setNewPosition(Position newPosition) {
            this.newPosition = newPosition;
        }

        PlayerCount getCount() {
            return count;
        }

        void clear() {
            count.clear();
        }

        @Override
        public TraversalContinuation visit(Position element) {
            if (element.equals(origin)) {
                return TraversalContinuation.SKIP;
            }

            GameCell cell = gameRepository.getCell(element);

            if (!cell.isInMyTerritory() && !element.equals(newPosition)) {
                return TraversalContinuation.SKIP;
            }

            Optional<PlayerBuilding> optBuilding = gameRepository.getPlayerBuildingAt(element);
            if (optBuilding.filter(building -> building.getType() == BuildingType.QG).isPresent()) {
                count.clear();
                return TraversalContinuation.STOP;
            }

            optBuilding.ifPresent(count::add);
            gameRepository.getPlayerUnitAt(element).ifPresent(count::add);

            count.size++;

            return TraversalContinuation.CONTINUE;
        }
    }

    static final class PlayerCount {

        int level1Count;
        int level2Count;
        int level3Count;
        int towerCount;
        int mineCount;
        int size;

        void clear() {
            level1Count = 0;
            level2Count = 0;
            level3Count = 0;
            towerCount = 0;
            mineCount = 0;
            size = 0;
        }

        void add(PlayerCount other) {
            level1Count += other.level1Count;
            level2Count += other.level2Count;
            level3Count += other.level3Count;
            towerCount += other.towerCount;
            mineCount += other.mineCount;
            size += other.size;
        }

        void add(PlayerUnit unit) {
            switch (unit.getLevel()) {
                case 1:
                    level1Count++;
                    break;
                case 2:
                    level2Count++;
                    break;
                case 3:
                    level3Count++;
                    break;
            }
        }

        void add(PlayerBuilding building) {
            if (building.getType() == BuildingType.TOWER) {
                towerCount++;
            } else if (building.getType() == BuildingType.MINE) {
                mineCount++;
            }
        }

        public int score() {
            int score = 0;
            score += level1Count * 2;
            score += level2Count * 5;
            score += level3Count * 8;
            score += towerCount;
            score += mineCount;
            score += size;
            return -score;
        }

        public static int noMoveScore() {
            return -9;
        }
    }

}

