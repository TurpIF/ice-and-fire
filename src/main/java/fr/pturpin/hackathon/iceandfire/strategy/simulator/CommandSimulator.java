package fr.pturpin.hackathon.iceandfire.strategy.simulator;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.List;

public interface CommandSimulator<T extends GameCommand> {

    void simulate(List<T> candidates);

}
