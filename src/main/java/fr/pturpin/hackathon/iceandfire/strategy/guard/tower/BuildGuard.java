package fr.pturpin.hackathon.iceandfire.strategy.guard.tower;

import fr.pturpin.hackathon.iceandfire.command.BuildCommand;

public interface BuildGuard {

    boolean isUseless(BuildCommand command);

}
