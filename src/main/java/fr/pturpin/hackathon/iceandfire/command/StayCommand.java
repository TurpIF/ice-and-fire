package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

public class StayCommand extends MoveCommand {

    public StayCommand(PlayerUnit unit, GameCell gameCell) {
        super(unit, gameCell);
    }

    @Override
    public void execute() {
        playerUnit.moveOn(gameCell);
    }

    @Override
    public boolean isValid() {
        return playerUnit.canMove();
    }

    @Override
    public String getFormattedCommand() {
        return "WAIT";
    }
}
