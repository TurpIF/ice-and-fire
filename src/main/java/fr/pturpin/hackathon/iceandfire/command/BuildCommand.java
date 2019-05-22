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
    public boolean isValid() {
        boolean hasEnoughMoney = gameRepository.getPlayerGold() >= trainedPlayerBuilding.getCost();
        boolean isBuildable = trainedPlayerBuilding.getBuildingType() != BuildingType.QG;
        return isBuildable
                && hasEnoughMoney
                && gameCell.isMineSpot()
                && !gameCell.isOccupied();
    }

    @Override
    public String getFormattedCommand() {
        Position position = gameCell.getPosition();
        return String.format("BUILD %s %d %d", trainedPlayerBuilding.getBuildingType(), position.getX(), position.getY());
    }
}
