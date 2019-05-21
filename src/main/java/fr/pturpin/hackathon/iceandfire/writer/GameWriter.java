package fr.pturpin.hackathon.iceandfire.writer;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GameWriter {

    private final PrintStream stream;
    private final List<GameCommand> currentCommands = new ArrayList<>();

    public GameWriter(PrintStream stream) {
        this.stream = stream;
    }

    public void startSequence() {
        currentCommands.clear();
    }

    public void add(GameCommand command) {
        currentCommands.add(command);
    }

    public void endSequence() {
        boolean isFirst = true;
        for (GameCommand command : currentCommands) {
            if (!isFirst) {
                stream.print(';');
            }
            isFirst = false;

            stream.print(command.getFormattedCommand());
        }

        stream.println();
    }
}
