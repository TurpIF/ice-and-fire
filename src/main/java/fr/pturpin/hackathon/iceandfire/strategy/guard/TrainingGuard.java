package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

public interface TrainingGuard {

    boolean isUseless(TrainCommand command);

}
