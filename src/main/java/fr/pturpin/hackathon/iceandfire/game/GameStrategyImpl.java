package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;

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
        addTrainCommands();
        return commands;
    }

    private void addCommand(GameCommand command) {
        if (command.isValid()) {
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

        // If I want to move but I'm next to an opponent that can't beat me and that I can't beat, then stay.
        {
            Collection<Position> neighbors = playerUnit.getPosition().getNeighbors();
            for (Position neighbor : neighbors) {
                Optional<OpponentUnit> optOpponentUnit = game.getOpponentUnitAt(neighbor);
                if (optOpponentUnit.isPresent()) {
                    OpponentUnit opponentUnit = optOpponentUnit.get();
                    if (!playerUnit.canBeat(opponentUnit)) {
                        boolean opponentBeatMe = new TrainedPlayerUnit(opponentUnit.getLevel()).canBeat(new OpponentUnit(playerUnit.getLevel()));
                        if (!opponentBeatMe) {
                            return false;
                        }
                    }
                }
            }
        }

        // If I want to move into my territory but an opponent is next to me, I guard the position.
        if (cell.isInMyTerritory()) {
            Collection<Position> neighbors = playerUnit.getPosition().getNeighbors();
            for (Position neighbor : neighbors) {
                if (game.getOpponentUnitAt(neighbor).isPresent()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void addTrainCommands() {
        TrainedPlayerUnit trainedPlayerUnit = new TrainedPlayerUnit(1);

        List<TrainCommand> candidates = getAllPositions()
                .map(game::getCell)
                .filter(GameCell::isInMyTerritoryOrInItsNeighborhood)
                .map(cell -> new TrainCommand(trainedPlayerUnit, cell, game))
                .filter(GameCommand::isValid)
                .filter(this::isTrainingUseful)
                .sorted(new TrainCommandComparator().reversed())
                .collect(Collectors.toList());

        candidates.forEach(this::addCommand);
    }

    private boolean isTrainingUseful(TrainCommand command) {
        // If the new unit will be next to the front line, then yes, else noe
        GameCell cell = command.getCell();

        int distance = distanceFromFrontLine.getDistanceOf(cell.getPosition());
        return distance <= 1;
    }

    private Stream<Position> getAllPositions() {
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

        private final Comparator<GameCell> comparator = new CellNearFrontLineComparator();

        @Override
        public int compare(MoveCommand o1, MoveCommand o2) {
            return comparator.compare(o1.getCell(), o2.getCell());
        }
    }

    private class TrainCommandComparator implements Comparator<TrainCommand> {

        private final Comparator<GameCell> comparator = new CellNearFrontLineComparator();

        @Override
        public int compare(TrainCommand o1, TrainCommand o2) {
            return comparator.compare(o1.getCell(), o2.getCell());
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
