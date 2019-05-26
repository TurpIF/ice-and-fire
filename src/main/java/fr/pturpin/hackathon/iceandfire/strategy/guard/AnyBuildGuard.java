package fr.pturpin.hackathon.iceandfire.strategy.guard;

import fr.pturpin.hackathon.iceandfire.command.BuildCommand;

import java.util.List;

public class AnyBuildGuard implements BuildGuard {

    private final List<BuildGuard> allGuards;

    public AnyBuildGuard(List<BuildGuard> allGuards) {
        this.allGuards = allGuards;
    }

    @Override
    public boolean isUseless(BuildCommand command) {
        return allGuards.stream().anyMatch(criteria -> criteria.isUseless(command));
    }

}