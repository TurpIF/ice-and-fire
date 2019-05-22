package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

public class MoveCommand implements GameCommand {

    private final PlayerUnit playerUnit;
    private final GameCell gameCell;

    public MoveCommand(PlayerUnit playerUnit, GameCell gameCell) {
        this.playerUnit = playerUnit;
        this.gameCell = gameCell;
    }

    @Override
    public boolean isValid() {
        return playerUnit.canMove()
                && gameCell.getPosition().distanceTo(playerUnit.getPosition()) == 1
                && !gameCell.containsAlly()
                && gameCell.containsBeatableOpponentFor(playerUnit);
    }

    @Override
    public String getFormattedCommand() {
        return String.format("MOVE %d %d %d", playerUnit.getId(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
