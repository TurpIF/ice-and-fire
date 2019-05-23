package fr.pturpin.hackathon.iceandfire.unit;

public class OpponentUnit {

    private final TrainedUnit trainedUnit;

    public OpponentUnit(int level) {
        this.trainedUnit = new TrainedUnit(level);
    }

    public OpponentUnit(TrainedUnit trainedUnit) {
        this.trainedUnit = trainedUnit;
    }

    public int getLevel() {
        return trainedUnit.getLevel();
    }

    TrainedUnit asTrainedUnit() {
        return trainedUnit;
    }

    public boolean canBeat(PlayerUnit playerUnit) {
        return trainedUnit.canBeat(playerUnit);
    }

    public boolean canBeat(TrainedUnit otherUnit) {
        return trainedUnit.canBeat(otherUnit);
    }
}
