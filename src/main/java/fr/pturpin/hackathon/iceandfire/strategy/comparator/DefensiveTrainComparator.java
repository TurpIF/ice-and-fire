package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

import java.util.Collection;
import java.util.Comparator;

public class DefensiveTrainComparator implements Comparator<TrainCommand> {

    private final GameRepository gameRepository;

    public DefensiveTrainComparator(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public int compare(TrainCommand o1, TrainCommand o2) {
        return Comparator.comparingInt(this::evaluate).compare(o1, o2);
    }

    private int evaluate(TrainCommand command) {
        return canKeepTieDefense(command) ? 1 : 0;
    }

    private boolean canKeepTieDefense(TrainCommand command) {
        GameCell cell = command.getCell();

        if (!cell.isInMyTerritory()) {
            return false;
        }

        TrainedUnit trainedUnit = command.getTrainedUnit();
        Collection<Position> neighbors = cell.getPosition().getNeighbors();

        for (Position neighbor : neighbors) {
            boolean isTie = gameRepository.getOpponentUnitAt(neighbor)
                    .filter(opponentUnit -> isTie(opponentUnit, trainedUnit))
                    .isPresent();

            if (isTie) {
                return true;
            }
        }

        return false;
    }

    private boolean isTie(OpponentUnit opponentUnit, TrainedUnit trainedUnit) {
        return !opponentUnit.canBeat(trainedUnit) && !trainedUnit.canBeat(opponentUnit);
    }
}
