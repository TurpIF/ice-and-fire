package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.command.TrainCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;

public class BeatableOpponentTrainComparator extends BeatableOpponentComparator<TrainCommand> {

    public BeatableOpponentTrainComparator(GameRepository gameRepository) {
        super(gameRepository);
    }

    @Override
    protected GameCell getCell(TrainCommand command) {
        return command.getCell();
    }

    @Override
    protected int evaluateScore(TrainCommand command, OpponentCount count) {
        int score = -2 * command.getTrainedUnit().getLevel();
        score += count.level1Count * 2;
        score += count.level2Count * 4;
        score += count.level3Count * 6;
        score += count.towerCount;
        score += count.mineCount;
        return score;
    }

}
