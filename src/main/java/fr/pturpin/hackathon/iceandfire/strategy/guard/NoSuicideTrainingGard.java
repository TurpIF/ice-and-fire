package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class NoSuicideTrainingGard implements TrainingGuard {

    private final GameRepository game;

    public NoSuicideTrainingGard(GameRepository game) {
        this.game = game;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        // If the new unit is in my territory but there is an opponent next to him that can beat it, then yes.
        GameCell cell = command.getCell();
        TrainedUnit trainedUnit = command.getTrainedUnit();

        if (cell.isInMyTerritory()) {
            Collection<Position> neighbors = cell.getPosition().getNeighbors();

            return neighbors.stream()
                    .map(game::getOpponentUnitAt)
                    .flatMap(this::stream)
                    .anyMatch(opponentUnit -> opponentUnit.canBeat(trainedUnit));
        }

        return false;
    }

    private <T> Stream<T> stream(Optional<T> optional) {
        return optional.map(Stream::of).orElseGet(Stream::empty);
    }
}
