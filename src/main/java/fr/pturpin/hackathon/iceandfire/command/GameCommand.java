package fr.pturpin.hackathon.iceandfire.command;

public interface GameCommand {

    void execute();

    boolean isValid();

    String getFormattedCommand();

}
