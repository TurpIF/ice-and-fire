package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.Position;

public class PlayerUnit {

    private final int id;
    private final Position position;
    private final TrainedPlayerUnit trainedPlayerUnit;

    public PlayerUnit(int id, Position position, TrainedPlayerUnit trainedPlayerUnit) {
        this.id = id;
        this.position = position;
        this.trainedPlayerUnit = trainedPlayerUnit;
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

    public boolean canBeat(OpponentUnit opponentUnit) {
        return trainedPlayerUnit.canBeat(opponentUnit);
    }

    public boolean canBeat(OpponentBuilding opponentBuilding) {
        return trainedPlayerUnit.canBeat(opponentBuilding);
    }

}
