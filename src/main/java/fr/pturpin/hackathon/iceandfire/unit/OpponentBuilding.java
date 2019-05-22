package fr.pturpin.hackathon.iceandfire.unit;

public class OpponentBuilding {

    private final BuildingType buildingType;

    public OpponentBuilding(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingType getType() {
        return buildingType;
    }

}
