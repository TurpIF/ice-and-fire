package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.BuildingType;

public class TrainedPlayerUnit {

    private final int level;

    public TrainedPlayerUnit(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean canBeat(OpponentUnit opponentUnit) {
        return level >= opponentUnit.getLevel();
    }

    public boolean canBeat(OpponentBuilding opponentBuilding) {
        return opponentBuilding.getType() == BuildingType.QG;
    }
}
