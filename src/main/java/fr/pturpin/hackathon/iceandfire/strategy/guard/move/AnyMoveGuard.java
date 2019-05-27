package fr.pturpin.hackathon.iceandfire.strategy.guard.move;

import fr.pturpin.hackathon.iceandfire.command.MoveCommand;

import java.util.List;

public class AnyMoveGuard implements MoveGuard {

    private final List<MoveGuard> allGuards;

    public AnyMoveGuard(List<MoveGuard> allGuards) {
        this.allGuards = allGuards;
    }

    @Override
    public boolean isUseless(MoveCommand command) {
        return allGuards.stream().anyMatch(criteria -> criteria.isUseless(command));
    }

}
