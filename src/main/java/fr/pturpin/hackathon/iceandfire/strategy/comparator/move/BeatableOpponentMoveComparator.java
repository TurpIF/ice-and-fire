package fr.pturpin.hackathon.iceandfire.strategy.comparator.move;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.BeatableOpponentComparator;
import fr.pturpin.hackathon.iceandfire.strategy.comparator.OpponentCount;

public class BeatableOpponentMoveComparator extends BeatableOpponentComparator<MoveCommand> {

    public BeatableOpponentMoveComparator(GameRepository gameRepository) {
        super(gameRepository);
    }

    @Override
    protected GameCell getCell(MoveCommand command) {
        return command.getCell();
    }

    @Override
    protected int evaluateScore(MoveCommand command, OpponentCount count) {
        int level = command.getPlayerUnit().getLevel();
        return count.score(level);
    }
}
