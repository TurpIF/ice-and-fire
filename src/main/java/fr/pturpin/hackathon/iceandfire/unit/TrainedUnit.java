package fr.pturpin.hackathon.iceandfire.unit;

public class TrainedUnit {

    private final int level;

    public TrainedUnit(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean canBeat(OpponentUnit opponentUnit) {
        return level > opponentUnit.getLevel() || level == 3;
    }

    public boolean canBeat(OpponentBuilding opponentBuilding) {
        return true;
    }

    public int getTrainingCost() {
        return level * 10;
    }
}
