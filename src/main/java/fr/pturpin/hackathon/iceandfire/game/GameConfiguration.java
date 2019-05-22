package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;

import java.util.Scanner;

public class GameConfiguration {

    private final GameWriter gameWriter;
    private final GameReader gameReader;
    private final Game game;

    public GameConfiguration() {
        gameWriter = new GameWriter(System.out);

        GameInputScanner inputSource = new GameInputScanner(new Scanner(System.in));
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
