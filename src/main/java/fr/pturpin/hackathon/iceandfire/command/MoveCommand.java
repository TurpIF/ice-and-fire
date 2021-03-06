package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

public class MoveCommand implements GameCommand {

    protected final PlayerUnit playerUnit;
    protected final GameCell gameCell;

    public MoveCommand(PlayerUnit playerUnit, GameCell gameCell) {
        this.playerUnit = playerUnit;
        this.gameCell = gameCell;
    }

    public GameCell getCell() {
        return gameCell;
    }

    public PlayerUnit getPlayerUnit() {
        return playerUnit;
    }

    @Override
    public void execute() {
        playerUnit.moveOn(gameCell);
    }

    @Override
    public boolean isValid() {
        return playerUnit.canMove()
                && !gameCell.isWall()
                && gameCell.getPosition().distanceTo(playerUnit.getPosition()) == 1
                && !gameCell.containsAlly()
                && gameCell.containsBeatableOpponentFor(playerUnit)
                && isNotProtectedByTower();
    }

    private boolean isNotProtectedByTower() {
        return !gameCell.isProtectedByOpponentTower() || playerUnit.canReachTower();
    }

    @Override
    public String getFormattedCommand() {
        return String.format("MOVE %d %d %d", playerUnit.getId(), gameCell.getPosition().getX(), gameCell.getPosition().getY());
    }
}
