package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameStrategyImpl implements GameStrategy {

    private final Game game;
    private List<GameCommand> commands = new ArrayList<>();

    public GameStrategyImpl(Game game) {
        this.game = game;
    }

    @Override
    public Collection<GameCommand> buildCommands() {
        commands.clear();
        addMoveCommands();
        addTrainCommands();
        return commands;
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

            if (command.isValid()) {
                candidates.add(command);
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        candidates.sort(new MoveCommandComparator().reversed());
        MoveCommand selected = candidates.get(0);

        commands.add(selected);
    }

    private void addTrainCommands() {
        TrainedPlayerUnit trainedPlayerUnit = new TrainedPlayerUnit(1);

        List<TrainCommand> candidates = getAllPositions()
                .map(game::getCell)
                .filter(GameCell::isInMyTerritoryOrInItsNeighborhood)
                .map(cell -> new TrainCommand(trainedPlayerUnit, cell, game))
                .filter(GameCommand::isValid)
                .sorted(new TrainCommandComparator().reversed())
                .collect(Collectors.toList());

        commands.addAll(candidates);
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
        @Override
        public int compare(MoveCommand o1, MoveCommand o2) {
            return Comparator.<GameCell>comparingInt(cell -> cell.isInMyTerritory() ? -1 : 0)
                    .thenComparing(GameCell::getPosition, new PositionComparator())
                    .compare(o1.getCell(), o2.getCell());
        }
    }

    private class TrainCommandComparator implements Comparator<TrainCommand> {
        @Override
        public int compare(TrainCommand o1, TrainCommand o2) {
            return Comparator.<GameCell>comparingInt(cell -> cell.isInMyTerritory() ? -1 : 0)
                    .thenComparing(GameCell::getPosition, new PositionComparator())
                    .compare(o1.getCell(), o2.getCell());
        }
    }
}
