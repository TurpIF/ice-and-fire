package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;

import java.util.Comparator;

public class TrainCommandComparator implements Comparator<TrainCommand> {

    private final GameRepository gameRepository;
    private final Comparator<TrainCommand> comparator;

    public TrainCommandComparator(GameRepository gameRepository, CellNearFrontLineComparator cellNearFrontLineComparator) {
        this.gameRepository = gameRepository;

        comparator = new BeatableOpponentTrainComparator(gameRepository)
                .thenComparing(new DefensiveTrainComparator(gameRepository))
                .thenComparingInt(command -> -command.getTrainedUnit().getLevel())
                .thenComparingInt(this::getLevelOfBeatableOpponent)
                .thenComparing(TrainCommand::getCell, cellNearFrontLineComparator);
    }

    private int getLevelOfBeatableOpponent(TrainCommand command) {
        return gameRepository.getOpponentUnitAt(command.getCell().getPosition())
                .filter(command.getTrainedUnit()::canBeat)
                .map(OpponentUnit::getLevel)
                .orElse(0);
    }

    @Override
    public int compare(TrainCommand o1, TrainCommand o2) {
        return comparator.compare(o1, o2);
    }
}
