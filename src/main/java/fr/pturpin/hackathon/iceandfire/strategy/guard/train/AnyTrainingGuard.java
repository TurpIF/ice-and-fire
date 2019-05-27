package fr.pturpin.hackathon.iceandfire.strategy.guard.train;

import fr.pturpin.hackathon.iceandfire.command.TrainCommand;

import java.util.List;

public class AnyTrainingGuard implements TrainingGuard {

    private final List<TrainingGuard> allGuards;

    public AnyTrainingGuard(List<TrainingGuard> allGuards) {
        this.allGuards = allGuards;
    }

    @Override
    public boolean isUseless(TrainCommand command) {
        return allGuards.stream().anyMatch(criteria -> criteria.isUseless(command));
    }
}
