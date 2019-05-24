package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.command.MoveCommand;

public interface MoveUsefulnessCriteria {

    boolean isUseless(MoveCommand command);

}
