package fr.pturpin.hackathon.iceandfire.unit;

public class TrainedUnit {

    private final int level;

    public TrainedUnit(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean canBeat(OpponentBuilding opponentBuilding) {
        if (opponentBuilding.getType() == BuildingType.TOWER) {
            return level == 3;
        }
        return true;
    }

    public boolean canBeat(OpponentUnit opponentUnit) {
        return canBeat(opponentUnit.asTrainedUnit());
    }

    public boolean canBeat(PlayerUnit playerUnit) {
        return canBeat(playerUnit.asTrainedUnit());
    }

    boolean canBeat(TrainedUnit otherUnit) {
        return level > otherUnit.getLevel() || level == 3;
    }

    public int getTrainingCost() {
        return level * 10;
    }

    public boolean canReachTower() {
        return level == 3;
    }
}
