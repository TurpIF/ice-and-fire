package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;

public class TrainCommand implements GameCommand {

    private final TrainedUnit trainedUnit;
    private final GameCell gameCell;
    private final GameRepository gameRepository;

    public TrainCommand(TrainedUnit trainedUnit, GameCell gameCell, GameRepository gameRepository) {
        this.trainedUnit = trainedUnit;
        this.gameCell = gameCell;
        this.gameRepository = gameRepository;
    }

    public GameCell getCell() {
        return gameCell;
    }

    @Override
    public boolean isValid() {
        boolean hasEnoughGold = gameRepository.getPlayerGold() >= trainedUnit.getTrainingCost();
        return hasEnoughGold
                && !gameCell.isWall()
                && !gameCell.containsAlly()
                && gameCell.isInMyTerritoryOrInItsNeighborhood()
                && gameCell.containsBeatableOpponentFor(trainedUnit);
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", trainedUnit.getLevel(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
