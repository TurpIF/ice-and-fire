package fr.pturpin.hackathon.iceandfire.command;

public class WaitCommand implements GameCommand {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getFormattedCommand() {
        return "WAIT";
    }

}
