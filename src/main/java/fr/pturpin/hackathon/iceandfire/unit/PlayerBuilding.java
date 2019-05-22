package fr.pturpin.hackathon.iceandfire.unit;

public class PlayerBuilding {

    private final BuildingType buildingType;

    public  PlayerBuilding(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingType getType() {
        return buildingType;
    }

}
