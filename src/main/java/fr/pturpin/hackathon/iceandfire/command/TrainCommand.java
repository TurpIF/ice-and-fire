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

    public TrainedUnit getTrainedUnit() {
        return trainedUnit;
    }

    @Override
    public void execute() {
        gameCell.invokeNewUnit(trainedUnit);
    }

    @Override
    public boolean isValid() {
        return !willNeverBeValidThisRound()
                && !gameCell.containsAlly()
                && gameCell.isInMyTerritoryOrInItsNeighborhood()
                && isNotProtectedByOpponentTower();
    }

    private boolean hasEnoughGold() {
        return gameRepository.getPlayerGold() >= trainedUnit.getTrainingCost();
    }

    public boolean willNeverBeValidThisRound() {
        return !(hasEnoughGold()
                && !gameCell.isWall()
                && gameCell.containsBeatableOpponentFor(trainedUnit));
    }

    private boolean isNotProtectedByOpponentTower() {
        return !gameCell.isProtectedByOpponentTower() || trainedUnit.canReachTower();
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", trainedUnit.getLevel(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
