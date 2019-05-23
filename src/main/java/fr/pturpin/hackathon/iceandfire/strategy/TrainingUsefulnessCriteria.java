package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

public interface TrainingUsefulnessCriteria {

    boolean isUseless(TrainCommand command);

}
