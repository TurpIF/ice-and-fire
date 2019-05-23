package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;

public class PlayerUnit {

    private final int id;
    private final TrainedUnit trainedUnit;
    private GameCell gameCell;
    private boolean canMove;

    public PlayerUnit(int id, GameCell gameCell, TrainedUnit trainedUnit) {
        this(id, gameCell, trainedUnit, true);
    }

    public PlayerUnit(int id, GameCell gameCell, TrainedUnit trainedUnit, boolean canMove) {
        this.id = id;
        this.gameCell = gameCell;
        this.trainedUnit = trainedUnit;
        this.canMove = canMove;
    }

    public boolean canMove() {
        return canMove;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return gameCell.getPosition();
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

    public boolean canReachTower() {
        return trainedUnit.canReachTower();
    }

    TrainedUnit asTrainedUnit() {
        return trainedUnit;
    }

    public void moveOn(GameCell newCell) {
        gameCell.removeLeavingUnit(this);
        newCell.setEnteringUnit(this);
        gameCell = newCell;
        canMove = false;
    }
}
