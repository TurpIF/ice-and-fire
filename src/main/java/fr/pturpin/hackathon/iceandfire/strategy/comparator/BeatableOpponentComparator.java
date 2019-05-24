package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;

import java.util.*;

public abstract class BeatableOpponentComparator<T extends GameCommand> implements Comparator<T> {

    private final GameRepository gameRepository;

    protected BeatableOpponentComparator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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

        for (Position neighbor : position.getNeighbors()) {
            GameCell neighborCell = gameRepository.getCell(neighbor);
            if (neighborCell.isInOpponentTerritory()) {
                count.add(getKillCountFrom(neighborCell.getPosition(), position));
            }
        }

        return count;
    }

    private OpponentCount getKillCountFrom(Position newVisit, Position origin) {
        Queue<Position> toVisit = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        OpponentCount count = new OpponentCount();

        toVisit.add(newVisit);
        visited.add(origin);

        while (!toVisit.isEmpty()) {
            Position current = toVisit.remove();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            GameCell currentCell = gameRepository.getCell(current);
            if (!currentCell.isInOpponentTerritory()) {
                continue;
            }

            Optional<OpponentBuilding> optBuilding = gameRepository.getOpponentBuildingAt(current);
            if (optBuilding.filter(building -> building.getType() == BuildingType.QG).isPresent()) {
                return new OpponentCount();
            }

            optBuilding.ifPresent(count::add);
            gameRepository.getOpponentUnitAt(current).ifPresent(count::add);

            toVisit.addAll(current.getNeighbors());
        }

        return count;
    }

    protected static final class OpponentCount {

        int level1Count;
        int level2Count;
        int level3Count;
        int towerCount;
        int mineCount;

        void add(OpponentCount other) {
            level1Count += other.level1Count;
            level2Count += other.level2Count;
            level3Count += other.level3Count;
            towerCount += towerCount;
            mineCount += mineCount;
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
            }
        }

    }
}
