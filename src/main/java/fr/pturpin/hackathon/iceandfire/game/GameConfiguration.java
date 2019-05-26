package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.reader.DebugInputSource;
import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameInputSource;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.strategy.GameStrategy;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;

import java.io.PrintStream;
import java.util.Scanner;

public class GameConfiguration {

    private static final boolean DEBUG = true;

    private final GameWriter gameWriter;
    private final GameReader gameReader;
    private final Game game;

    public GameConfiguration(Scanner in, PrintStream out) {
        gameWriter = new GameWriter(out);

        GameInputSource inputSource = new GameInputScanner(in);
        if (DEBUG) {
            inputSource = new DebugInputSource(inputSource, System.err);
        }
        gameReader = new GameReader(inputSource);

        game = new Game();
    }

    public GameWriter gameWriter() {
        return gameWriter;
    }

    public GameReader gameReader() {
        return gameReader;
    }

    public GameInitialization gameInitialization() {
        return game.onInitialization();
    }

    public GameNewTurn gameNewTurn() {
        return game.onNewTurn();
    }

    public GameStrategy gameStrategy() {
        return game.asStrategy();
    }

}
