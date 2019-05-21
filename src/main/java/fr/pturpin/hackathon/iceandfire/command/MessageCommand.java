package fr.pturpin.hackathon.iceandfire.command;

public class MessageCommand implements GameCommand {

    private final String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getFormattedCommand() {
        return "MSG " + message;
    }
}
