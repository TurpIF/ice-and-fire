package fr.pturpin.hackathon.iceandfire.strategy.generator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainCommandGenerator implements CommandGenerator<TrainCommand> {

    private final TrainedUnit trainedUnit1 = new TrainedUnit(1);
    private final TrainedUnit trainedUnit2 = new TrainedUnit(2);
    private final TrainedUnit trainedUnit3 = new TrainedUnit(3);

    private final GameRepository game;

    public TrainCommandGenerator(GameRepository gameRepository) {
        game = gameRepository;
    }

    @Override
    public List<TrainCommand> generate() {
        return game.getAllCells()
                .flatMap(this::generate)
                .collect(Collectors.toList());
    }

    private Stream<TrainCommand> generate(GameCell cell) {
        return Stream.of(
                new TrainCommand(trainedUnit1, cell, game),
                new TrainCommand(trainedUnit2, cell, game),
                new TrainCommand(trainedUnit3, cell, game));
    }

}
