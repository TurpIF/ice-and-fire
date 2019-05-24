package fr.pturpin.hackathon.iceandfire.strategy.generator;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.List;

public interface CommandGenerator<T extends GameCommand> {

    List<T> generate();

}
