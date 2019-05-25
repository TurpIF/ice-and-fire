package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

import java.util.*;

public class NotInIsolatedNeutralZoneTrainingGuard implements TrainingGuard {

    private final GameRepository gameRepository;

    public NotInIsolatedNeutralZoneTrainingGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If there is a neutral zone without any contact with opponent zone,
        // and there is a unit nearby, then it's useless.

        GameCell cell = command.getCell();
        Position position = cell.getPosition();

        if (!isAllyUnitNearby(position)) {
            return false;
        }

        return isOnlyNeutralZone(position);
    }

    private boolean isAllyUnitNearby(Position position) {
        return isAllyUnitNearby(position, 3);
    }

    private boolean isAllyUnitNearby(Position position, int distance) {
        if (distance == 0) {
            return false;
        }

        Collection<Position> neighbors = position.getNeighbors();

        for (Position neighbor : neighbors) {
            boolean isUnitPresent = gameRepository.getPlayerUnitAt(neighbor).isPresent();
            if (isUnitPresent) {
                return true;
            }

            if (isAllyUnitNearby(neighbor, distance - 1)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOnlyNeutralZone(Position position) {
        Queue<Position> toVisit = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();

        toVisit.add(position);

        while (!toVisit.isEmpty()) {
            Position current = toVisit.remove();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (isWall(current)) {
                continue;
            }

            if (isNotNeural(current)) {
                return false;
            }

            toVisit.addAll(current.getNeighbors());
        }

        return true;
    }

    private boolean isNotNeural(Position position) {
        List<CellType> neutralLike = Arrays.asList(CellType.NEUTRAL, CellType.INACTIVE_THEIR);
        CellType cellType = gameRepository.getCellType(position);
        return !neutralLike.contains(cellType);
    }

    private boolean isWall(Position position) {
        List<CellType> wallLike = Arrays.asList(CellType.NIL, CellType.ACTIVE_MINE, CellType.INACTIVE_MINE);
        CellType cellType = gameRepository.getCellType(position);
        return wallLike.contains(cellType);
    }

}
