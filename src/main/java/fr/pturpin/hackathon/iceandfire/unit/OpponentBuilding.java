package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.Position;

public class OpponentBuilding {

    private final BuildingType buildingType;
    private final Position position;

    public OpponentBuilding(BuildingType buildingType, Position position) {
        this.buildingType = buildingType;
        this.position = position;
    }

    public BuildingType getType() {
        return buildingType;
    }

    public Position getPosition() {
        return position;
    }
}
