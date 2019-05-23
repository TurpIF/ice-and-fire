package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.Game;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameStrategyImpl implements GameStrategy {

    private final Game game;
    private List<GameCommand> commands = new ArrayList<>();
    private DistanceFromFrontLine distanceFromFrontLine;
    private DistanceFromOpponentCastle distanceFromOpponentCastle;

    public GameStrategyImpl(Game game) {
        this.game = game;
    }

    @Override
    public Collection<GameCommand> buildCommands() {
        computeDistances();

        commands.clear();
        addMoveCommands();
        addTrainCommands(1);
        addTrainCommands(2);
        addTrainCommands(3);
        return commands;
    }

    private void addCommand(GameCommand command) {
        if (command.isValid()) {
            // TODO sequentially simulates command so further ones are accurate
            //  If done, then commands should be ordered to maximize a winning criteria.
            //  Training commands' generation should be be bulked.

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
        // FIXME repository should provide a getPlayerUnits method
        getAllPositions().map(game::getPlayerUnitAt)
                .flatMap(this::stream)
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
        GameCell cell = command.getCell();
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

    private void addTrainCommands(int level) {
        TrainedUnit trainedUnit = new TrainedUnit(level);

        List<TrainCommand> candidates = getAllPositions()
                .map(game::getCell)
                .filter(GameCell::isInMyTerritoryOrInItsNeighborhood)
                .map(cell -> new TrainCommand(trainedUnit, cell, game))
                .filter(GameCommand::isValid)
                .filter(this::isTrainingUseful)
                .sorted(new TrainCommandComparator().reversed())
                .collect(Collectors.toList());

        candidates.forEach(this::addCommand);
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

    private Stream<Position> getAllPositions() {
        // FIXME repository should provide a method getAllCells
        return IntStream.range(0, 12).boxed().flatMap(x ->
                IntStream.range(0, 12).mapToObj(y -> new Position(x, y)));
    }

    private <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
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
            comparator = Comparator.comparingInt(this::getLevelOfBeatableOpponent)
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
