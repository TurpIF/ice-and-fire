package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.game.GameConfiguration;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

public class GameTask {
    public void solve(int testNumber, Scanner in, PrintStream out) {
        // CHelper adapter.
        Application application = new Application(new GameConfiguration(in, out));

        application.init();

        while (true) {
            application.update();
            Collection<GameCommand> commands = application.buildCommands();
            application.printCommands(commands);
        }
    }
}
