package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.graph.DfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.PositionDfsTraversal;
import fr.pturpin.hackathon.iceandfire.strategy.graph.TraversalVisitor;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

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
        GameCell cell = getCell(command);
        OpponentCount count = getBeatableOpponentCount(cell);

        int neighborScore = getNeighborScore(command, cell);

        return evaluateScore(command, count) + neighborScore;
    }

    private int getNeighborScore(T command, GameCell cell) {
        TrainedUnit trainedUnit = new TrainedUnit(1);
        int trainingCost = trainedUnit.getTrainingCost();
        if (command instanceof TrainCommand) {
            TrainedUnit unit = ((TrainCommand) command).getTrainedUnit();
            trainingCost += unit.getTrainingCost();
        }

        int neighborScore = 0;
        if (gameRepository.getPlayerGold() >= trainingCost) {
            Collection<Position> neighbors = cell.getPosition().getNeighbors();
            for (Position neighbor : neighbors) {
                GameCell neighborCell = gameRepository.getCell(neighbor);
                OpponentCount neighborCount = getBeatableOpponentCount(neighborCell);
                int score = neighborCount.score(1);
                if (score > neighborScore) {
                    neighborScore = score;
                }
            }
        }
        return neighborScore;
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

}
