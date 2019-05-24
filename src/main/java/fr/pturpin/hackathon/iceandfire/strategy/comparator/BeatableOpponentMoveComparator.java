package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

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
        int score = -2 * command.getPlayerUnit().getLevel();
        score += count.level1Count * 2;
        score += count.level2Count * 4;
        score += count.level3Count * 6;
        score += count.towerCount;
        score += count.mineCount;
        return score;
    }
}
