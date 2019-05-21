package fr.pturpin.hackathon.iceandfire.command;

public interface GameCommand {

    boolean isValid();

    String getFormattedCommand();

}
