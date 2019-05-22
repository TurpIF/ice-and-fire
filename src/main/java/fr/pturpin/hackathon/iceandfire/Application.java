package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.WaitCommand;
import fr.pturpin.hackathon.iceandfire.game.GameConfiguration;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;

import java.util.Collection;
import java.util.Collections;

public class Application {

    private final GameConfiguration configuration;

    public Application(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    public void init() {
        configuration.gameReader().readInit(configuration.gameInitialization());
    }

    public void update() {
        configuration.gameReader().readNewTurn(configuration.gameNewTurn());
    }

    public Collection<GameCommand> buildCommands() {
        return configuration.gameStrategy().buildCommands();
    }

    public void printCommands(Collection<GameCommand> commands) {
        if (commands.isEmpty()) {
            printCommands(Collections.singletonList(new WaitCommand()));
            return;
        }

        GameWriter writer = configuration.gameWriter();

        writer.startSequence();
        commands.forEach(writer::add);
        writer.endSequence();
    }

    public static void main(String[] args) {
        Application application = new Application(new GameConfiguration());

        application.init();

        while (true) {
            application.update();
            Collection<GameCommand> commands = application.buildCommands();
            application.printCommands(commands);
        }
    }

}
