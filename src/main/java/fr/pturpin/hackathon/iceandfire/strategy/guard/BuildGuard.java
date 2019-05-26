package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.command.BuildCommand;

public interface BuildGuard {

    boolean isUseless(BuildCommand command);

}
