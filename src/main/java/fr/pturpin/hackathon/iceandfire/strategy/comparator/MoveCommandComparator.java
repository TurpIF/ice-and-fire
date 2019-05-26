package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;

import java.util.Comparator;

public class MoveCommandComparator implements Comparator<MoveCommand> {

    private final GameRepository gameRepository;
    private final Comparator<MoveCommand> comparator;

    public MoveCommandComparator(GameRepository gameRepository, CellNearFrontLineComparator cellNearFrontLineComparator) {
        this.gameRepository = gameRepository;

        comparator = new BeatableOpponentMoveComparator(gameRepository)
                .thenComparing(new DefensiveMoveComparator(gameRepository))
                .thenComparingInt(this::getLevelOfBeatableOpponent)
                .thenComparing(MoveCommand::getCell, cellNearFrontLineComparator);
    }

    private int getLevelOfBeatableOpponent(MoveCommand command) {
        return gameRepository.getOpponentUnitAt(command.getCell().getPosition())
                .filter(command.getPlayerUnit()::canBeat)
                .map(OpponentUnit::getLevel)
                .orElse(0);
    }

    @Override
    public int compare(MoveCommand o1, MoveCommand o2) {
        return comparator.compare(o1, o2);
    }
}
