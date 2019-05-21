package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Game;
import fr.pturpin.hackathon.iceandfire.GameCell;

public class TrainCommand implements GameCommand {

    private final int level;
    private final GameCell gameCell;
    private final Game game;

    public TrainCommand(int level, GameCell gameCell, Game game) {
        this.level = level;
        this.gameCell = gameCell;
        this.game = game;
    }

    @Override
    public boolean isValid() {
        boolean hasEnoughGold = game.getPlayerGold() >= 10;
        return hasEnoughGold
                && !gameCell.containsAlly()
                && gameCell.isInMyTerritoryOrInItsNeighborhood()
                && gameCell.containsBeatableOpponentForLevel(level);

        // TODO In case of unit or building of the opponent on the case,
        //  the trained unit should be able to defeat them. Else the command is not valid and ignored.
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", level, gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
