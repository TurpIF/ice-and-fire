package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;

public class TrainCommand implements GameCommand {

    private final TrainedPlayerUnit trainedPlayerUnit;
    private final GameCell gameCell;
    private final GameRepository gameRepository;

    public TrainCommand(TrainedPlayerUnit trainedPlayerUnit, GameCell gameCell, GameRepository gameRepository) {
        this.trainedPlayerUnit = trainedPlayerUnit;
        this.gameCell = gameCell;
        this.gameRepository = gameRepository;
    }

    public GameCell getCell() {
        return gameCell;
    }

    @Override
    public boolean isValid() {
        boolean hasEnoughGold = gameRepository.getPlayerGold() >= 10;
        return hasEnoughGold
                && !gameCell.isWall()
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
