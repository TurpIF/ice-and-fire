package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.command.MoveCommand;

public interface MoveGuard {

    boolean isUseless(MoveCommand command);

}
