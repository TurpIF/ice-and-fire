package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameStrategyImpl implements GameStrategy {

    private final GameRepository game;
    private List<GameCommand> commands = new ArrayList<>();
    private DistanceFromFrontLine distanceFromFrontLine;
    private DistanceFromOpponentCastle distanceFromOpponentCastle;

    public GameStrategyImpl(GameRepository game) {
        this.game = game;
    }

    @Override
    public Collection<GameCommand> buildCommands() {
        computeDistances();

        commands.clear();
        addMoveCommands();
        addTrainCommands();
        return commands;
    }

    private void addCommand(GameCommand command) {
        if (command.isValid()) {
            command.execute();
            commands.add(command);
        }
    }

    private void computeDistances() {
        distanceFromFrontLine = new DistanceFromFrontLine(game);
        distanceFromFrontLine.compute();

        distanceFromOpponentCastle = new DistanceFromOpponentCastle(game);
        distanceFromOpponentCastle.compute();
    }

    private void addMoveCommands() {
        game.getAllPlayerUnits()
                .sorted(Comparator.comparingInt(playerUnit -> -distanceFromFrontLine.getDistanceOf(playerUnit.getPosition())))
                .forEach(this::addMoveCommand);
    }

    private void addMoveCommand(PlayerUnit unit) {
        Collection<Position> neighbors = unit.getPosition().getNeighbors();
        List<MoveCommand> candidates = new ArrayList<>();

        for (Position neighbor : neighbors) {
            GameCell neighborCell = game.getCell(neighbor);
            MoveCommand command = new MoveCommand(unit, neighborCell);

            if (command.isValid() && isMoveUseful(command)) {
                candidates.add(command);
            }
        }

        candidates.stream()
                .max(new MoveCommandComparator())
                .ifPresent(this::addCommand);
    }

    private boolean isMoveUseful(MoveCommand command) {
        PlayerUnit playerUnit = command.getPlayerUnit();

        // If I'm next to an opponent that can't beat me and that I can't beat, then stay.
        {
            Collection<Position> neighbors = playerUnit.getPosition().getNeighbors();
            for (Position neighbor : neighbors) {
                Optional<OpponentUnit> optOpponentUnit = game.getOpponentUnitAt(neighbor);
                if (optOpponentUnit.isPresent()) {
                    OpponentUnit opponentUnit = optOpponentUnit.get();
                    boolean isTie = !playerUnit.canBeat(opponentUnit) && !opponentUnit.canBeat(playerUnit);
                    if (isTie) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void addTrainCommands() {
        TrainedUnit trainedUnit1 = new TrainedUnit(1);
        TrainedUnit trainedUnit2 = new TrainedUnit(2);
        TrainedUnit trainedUnit3 = new TrainedUnit(3);

        List<TrainCommand> candidates = game.getAllCells()
                .flatMap(cell -> Stream.of(
                        new TrainCommand(trainedUnit1, cell, game),
                        new TrainCommand(trainedUnit2, cell, game),
                        new TrainCommand(trainedUnit3, cell, game)))
                .filter(command -> !command.willNeverBeValidThisRound())
                .collect(Collectors.toList());

        Comparator<TrainCommand> comparator = new TrainCommandComparator();
        candidates.sort(comparator);

        List<TrainCommand> ignored = new ArrayList<>();

        while (!candidates.isEmpty()) {
            TrainCommand command = candidates.remove(candidates.size() - 1);

            if (command.isValid() && isTrainingUseful(command)) {
                addCommand(command);

                candidates.addAll(ignored);
                candidates.sort(comparator);

                ignored.clear();
            } else if (!command.willNeverBeValidThisRound()) {
                ignored.add(command);
            }
        }
    }

    private boolean isTrainingUseful(TrainCommand command) {
        List<TrainingUsefulnessCriteria> trainingUsefulnessCriteria = Arrays.asList(
                new NextToFrontLineTrainingCriteria(distanceFromFrontLine),
                new NoSuicideTrainingCriteria(game));

        for (TrainingUsefulnessCriteria criteria : trainingUsefulnessCriteria) {
            if (criteria.isUseless(command)) {
                return false;
            }
        }

        return true;
    }

    private static final class PositionComparator implements Comparator<Position> {
        @Override
        public int compare(Position o1, Position o2) {
            return Comparator.comparingInt(Position::getX).thenComparingInt(Position::getY).compare(o1, o2);
        }
    }

    private class MoveCommandComparator implements Comparator<MoveCommand> {

        private final Comparator<MoveCommand> comparator;

        private MoveCommandComparator() {
            comparator = Comparator.comparingInt(this::getLevelOfBeatableOpponent)
                    .thenComparing(MoveCommand::getCell, new CellNearFrontLineComparator());
        }

        private int getLevelOfBeatableOpponent(MoveCommand command) {
            return game.getOpponentUnitAt(command.getCell().getPosition())
                    .filter(command.getPlayerUnit()::canBeat)
                    .map(OpponentUnit::getLevel)
                    .orElse(0);
        }

        @Override
        public int compare(MoveCommand o1, MoveCommand o2) {
            return comparator.compare(o1, o2);
        }
    }

    private class TrainCommandComparator implements Comparator<TrainCommand> {

        private Comparator<TrainCommand> comparator;

        private TrainCommandComparator() {
            comparator = Comparator.<TrainCommand>comparingInt(command -> -command.getTrainedUnit().getLevel())
                    .thenComparingInt(this::getLevelOfBeatableOpponent)
                    .thenComparing(TrainCommand::getCell, new CellNearFrontLineComparator());
        }

        private int getLevelOfBeatableOpponent(TrainCommand command) {
            return game.getOpponentUnitAt(command.getCell().getPosition())
                    .filter(command.getTrainedUnit()::canBeat)
                    .map(OpponentUnit::getLevel)
                    .orElse(0);
        }

        @Override
        public int compare(TrainCommand o1, TrainCommand o2) {
            return comparator.compare(o1, o2);
        }
    }

    private class CellNearFrontLineComparator implements Comparator<GameCell> {

        private Comparator<GameCell> comparator;

        private CellNearFrontLineComparator() {
            comparator = Comparator.<GameCell>comparingInt(cell -> cell.isInMyTerritory() ? -1 : 0)
                    .thenComparingInt(cell -> -distanceFromOpponentCastle.getDistanceOf(cell.getPosition()))
                    .thenComparingInt(cell -> -distanceFromFrontLine.getDistanceOf(cell.getPosition()))
                    .thenComparing(GameCell::getPosition, new PositionComparator());
        }

        @Override
        public int compare(GameCell o1, GameCell o2) {
            return comparator.compare(o1, o2);
        }
    }
}
