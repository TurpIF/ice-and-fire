package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.graph.DfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.PositionDfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.TraversalVisitor;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class NotInIsolatedNeutralZoneTrainingGuard implements TrainingGuard {

    private static final int MIN_DISTANCE_BETWEEN_UNITS = 3;
    private static final int MIN_SIZE_OF_USEFUL_ZONE = 3;

    private final GameRepository gameRepository;

    private final OnlyNeutralZoneVisitor visitor;
    private final DfsTraversal<Position> traversal;

    public NotInIsolatedNeutralZoneTrainingGuard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        visitor = new OnlyNeutralZoneVisitor();
        traversal = new PositionDfsTraversal();
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If there is a neutral zone without any contact with opponent zone,
        // and there is a unit nearby, then it's useless.

        GameCell cell = command.getCell();
        Position position = cell.getPosition();
        TrainedUnit trainedUnit = command.getTrainedUnit();

        VisitedZone zone = getZone(position);

        if (zone.isOnlyNeutral) {
            boolean isOverpowered = trainedUnit.getLevel() != 1;
            if (isOverpowered) {
                return true;
            }

            if (isAllyUnitNearby(position)) {
                return true;
            }

            // Is not worth it
            return zone.size < MIN_SIZE_OF_USEFUL_ZONE;
        }

        return false;

    }

    private boolean isAllyUnitNearby(Position position) {
        return isAllyUnitNearby(position, MIN_DISTANCE_BETWEEN_UNITS);
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

    private VisitedZone getZone(Position position) {
        visitor.clear();
        traversal.traverse(position, visitor);
        return visitor.getZone();
    }

    private class OnlyNeutralZoneVisitor implements TraversalVisitor<Position> {

        private VisitedZone zone = new VisitedZone();

        @Override
        public TraversalContinuation visit(Position element) {
            if (isWall(element)) {
                return TraversalContinuation.SKIP;
            } else if (isNotNeural(element)) {
                visitNotNeutral();
                return TraversalContinuation.STOP;
            }
            visitNeutral();
            return TraversalContinuation.CONTINUE;
        }

        void clear() {
            zone = new VisitedZone();
        }

        VisitedZone getZone() {
            return zone;
        }

        private void visitNeutral() {
            zone.size++;
        }

        private void visitNotNeutral() {
            zone.isOnlyNeutral = false;
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

    private class VisitedZone {

        private boolean isOnlyNeutral = true;
        private int size = 0;

    }

}
