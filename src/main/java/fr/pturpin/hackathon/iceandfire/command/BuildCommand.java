package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerBuilding;

public class BuildCommand implements GameCommand {

    private final TrainedPlayerBuilding trainedPlayerBuilding;
    private final GameCell gameCell;
    private final GameRepository gameRepository;

    public BuildCommand(TrainedPlayerBuilding trainedPlayerBuilding, GameCell gameCell, GameRepository gameRepository) {
        this.trainedPlayerBuilding = trainedPlayerBuilding;
        this.gameCell = gameCell;
        this.gameRepository = gameRepository;
    }

    @Override
    public void execute() {
        gameRepository.invokeNewBuilding(trainedPlayerBuilding, gameCell);
    }

    @Override
    public boolean isValid() {
        return isBuildable()
                && hasEnoughMoney()
                && !gameCell.isOccupied();
    }

    private boolean hasEnoughMoney() {
        return gameRepository.getPlayerGold() >= trainedPlayerBuilding.getCost();
    }

    private boolean isBuildable() {
        BuildingType buildingType = trainedPlayerBuilding.getBuildingType();
        if (buildingType == BuildingType.QG) {
            return false;
        } else if (buildingType == BuildingType.MINE) {
            return gameCell.isMineSpot();
        } else if (buildingType == BuildingType.TOWER) {
            return !gameCell.isMineSpot();
        }
        return true;
    }

    @Override
    public String getFormattedCommand() {
        Position position = gameCell.getPosition();
        return String.format("BUILD %s %d %d", trainedPlayerBuilding.getBuildingType(), position.getX(), position.getY());
    }
}
