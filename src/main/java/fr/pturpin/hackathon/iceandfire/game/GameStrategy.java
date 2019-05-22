package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.util.Collection;

public interface GameStrategy {

    Collection<GameCommand> buildCommands();

}
