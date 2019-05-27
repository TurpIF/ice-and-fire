package fr.pturpin.hackathon.iceandfire.strategy.guard.train;

import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

public interface TrainingGuard {

    boolean isUseless(TrainCommand command);

}
