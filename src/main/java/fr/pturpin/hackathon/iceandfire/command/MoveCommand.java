package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Position;

public class MoveCommand implements GameCommand {

    private final int unitId;
    private final Position targetPosition;

    public MoveCommand(int unitId, Position targetPosition) {
        this.unitId = unitId;
        this.targetPosition = targetPosition;
    }

    @Override
    public String getFormattedCommand() {
        return String.format("MOVE %d %d %d", unitId, targetPosition.getX(), targetPosition.getY());
    }
}
