package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.BuildingType;

public class OpponentBuilding {

    private final BuildingType buildingType;

    public OpponentBuilding(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingType getType() {
        return buildingType;
    }

}
