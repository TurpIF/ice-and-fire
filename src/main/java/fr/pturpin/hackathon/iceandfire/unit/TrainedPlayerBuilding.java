package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;

public class TrainedPlayerBuilding {

    private final BuildingType buildingType;
    private final GameRepository gameRepository;

    public TrainedPlayerBuilding(BuildingType buildingType, GameRepository gameRepository) {
        this.buildingType = buildingType;
        this.gameRepository = gameRepository;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public int getCost() {
        return 20 + 4 * gameRepository.getPlayerMineCount();
    }
}
