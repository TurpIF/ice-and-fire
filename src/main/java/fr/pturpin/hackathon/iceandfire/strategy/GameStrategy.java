package fr.pturpin.hackathon.iceandfire.strategy;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.Collection;

public interface GameStrategy {

    Collection<GameCommand> buildCommands();

}
