package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Game;
import fr.pturpin.hackathon.iceandfire.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;

public class TrainCommand implements GameCommand {

    private final TrainedPlayerUnit trainedPlayerUnit;
    private final GameCell gameCell;
    private final Game game;

    public TrainCommand(TrainedPlayerUnit trainedPlayerUnit, GameCell gameCell, Game game) {
        this.trainedPlayerUnit = trainedPlayerUnit;
        this.gameCell = gameCell;
        this.game = game;
    }

    @Override
    public boolean isValid() {
        boolean hasEnoughGold = game.getPlayerGold() >= 10;
        return hasEnoughGold
                && !gameCell.containsAlly()
                && gameCell.isInMyTerritoryOrInItsNeighborhood()
                && gameCell.containsBeatableOpponentFor(trainedPlayerUnit);

        // TODO In case of unit or building of the opponent on the case,
        //  the trained unit should be able to defeat them. Else the command is not valid and ignored.
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", trainedPlayerUnit.getLevel(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
