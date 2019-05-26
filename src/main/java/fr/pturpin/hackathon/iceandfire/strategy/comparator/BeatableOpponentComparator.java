package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.graph.DfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.PositionDfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.TraversalVisitor;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;

import java.util.*;

public abstract class BeatableOpponentComparator<T extends GameCommand> implements Comparator<T> {

    private final GameRepository gameRepository;

    private final DfsTraversal<Position> traversal;
    private final KillCountVisitor visitor;

    protected BeatableOpponentComparator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        traversal = new PositionDfsTraversal();
        visitor = new KillCountVisitor();
    }

    @Override
    public int compare(T o1, T o2) {
        return Comparator.<T>comparingInt(this::evaluateScore).compare(o1, o2);
    }

    private int evaluateScore(T command) {
        OpponentCount count = getBeatableOpponentCount(getCell(command));
        return evaluateScore(command, count);
    }

    protected abstract GameCell getCell(T command);

    protected abstract int evaluateScore(T command, OpponentCount count);

    private OpponentCount getBeatableOpponentCount(GameCell cell) {
        OpponentCount count = new OpponentCount();

        if (!cell.isInOpponentTerritory()) {
            return count;
        }


        Position position = cell.getPosition();
        gameRepository.getOpponentUnitAt(position).ifPresent(count::add);
        gameRepository.getOpponentBuildingAt(position).ifPresent(count::add);

        for (Position neighbor : position.getNeighbors()) {
            GameCell neighborCell = gameRepository.getCell(neighbor);
            if (neighborCell.isInOpponentTerritory()) {
                count.add(getKillCountFrom(neighborCell.getPosition(), position));
            }
        }

        return count;
    }

    private OpponentCount getKillCountFrom(Position newVisit, Position origin) {
        visitor.clear();
        visitor.setOrigin(origin);
        traversal.traverse(newVisit, visitor);
        return visitor.getCount();
    }

    private class KillCountVisitor implements TraversalVisitor<Position> {

        private OpponentCount count = new OpponentCount();
        private Position origin;

        void setOrigin(Position origin) {
            this.origin = origin;
        }

        OpponentCount getCount() {
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

            if (!cell.isInOpponentTerritory()) {
                return TraversalContinuation.SKIP;
            }

            Optional<OpponentBuilding> optBuilding = gameRepository.getOpponentBuildingAt(element);
            if (optBuilding.filter(building -> building.getType() == BuildingType.QG).isPresent()) {
                count.clear();
                return TraversalContinuation.STOP;
            }

            optBuilding.ifPresent(count::add);
            gameRepository.getOpponentUnitAt(element).ifPresent(count::add);

            count.size++;

            return TraversalContinuation.CONTINUE;
        }
    }

    static final class OpponentCount {

        int level1Count;
        int level2Count;
        int level3Count;
        int towerCount;
        int mineCount;
        int qgCount;
        int size;

        void clear() {
            level1Count = 0;
            level2Count = 0;
            level3Count = 0;
            towerCount = 0;
            mineCount = 0;
            size = 0;
        }

        void add(OpponentCount other) {
            level1Count += other.level1Count;
            level2Count += other.level2Count;
            level3Count += other.level3Count;
            towerCount += other.towerCount;
            mineCount += other.mineCount;
            size += other.size;
        }

        void add(OpponentUnit unit) {
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

        void add(OpponentBuilding building) {
            if (building.getType() == BuildingType.TOWER) {
                towerCount++;
            } else if (building.getType() == BuildingType.MINE) {
                mineCount++;
            } else if (building.getType() == BuildingType.QG) {
                qgCount++;
            }
        }

        public int score(int level) {
            int score = -5 * level;
            score += level1Count * 2;
            score += level2Count * 5;
            score += level3Count * 8;
            score += towerCount;
            score += mineCount;
            score += size;
            score += qgCount * 1000;
            return score;
        }
    }
}
