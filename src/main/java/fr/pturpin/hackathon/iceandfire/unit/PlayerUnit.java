package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.Position;

public class PlayerUnit {

    private final int id;
    private final Position position;
    private final TrainedUnit trainedUnit;

    public PlayerUnit(int id, Position position, TrainedUnit trainedUnit) {
        this.id = id;
        this.position = position;
        this.trainedUnit = trainedUnit;
    }

    public boolean canMove() {
        return true;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public int getLevel() {
        return trainedUnit.getLevel();
    }

    public boolean canBeat(OpponentUnit opponentUnit) {
        return trainedUnit.canBeat(opponentUnit);
    }

    public boolean canBeat(OpponentBuilding opponentBuilding) {
        return trainedUnit.canBeat(opponentBuilding);
    }
}
