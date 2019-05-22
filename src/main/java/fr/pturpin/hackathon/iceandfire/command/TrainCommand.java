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
        boolean hasEnoughGold = gameRepository.getPlayerGold() >= trainedPlayerUnit.getTrainingCost();
        return hasEnoughGold
                && !gameCell.isWall()
                && !gameCell.containsAlly()
                && gameCell.isInMyTerritoryOrInItsNeighborhood()
                && gameCell.containsBeatableOpponentFor(trainedPlayerUnit);
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", trainedPlayerUnit.getLevel(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
