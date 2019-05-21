package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Position;

public class TrainCommand implements GameCommand {

    private final int level;
    private final Position targetPosition;

    public TrainCommand(int level, Position targetPosition) {
        this.level = level;
        this.targetPosition = targetPosition;
    }

    @Override
    public String getFormattedCommand() {
        return String.format("TRAIN %d %d %d", level, targetPosition.getX(), targetPosition.getY());
    }
}
