package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.Application;
import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Scanner;

public class GameConfiguration {

    private final GameWriter gameWriter;
    private final GameReader gameReader;
    private final GameInitialization gameInitialization;
    private final GameNewTurn gameNewTurn;
    private final GameStrategy gameStrategy;

    public GameConfiguration() {
        gameWriter = new GameWriter(System.out);

        GameInputScanner inputSource = new GameInputScanner(new Scanner(System.in));
        gameReader = new GameReader(inputSource);

        gameInitialization = mock(GameInitialization.class);

        gameNewTurn = mock(GameNewTurn.class);

        gameStrategy = mock(GameStrategy.class);
    }

    public GameWriter gameWriter() {
        return gameWriter;
    }

    public GameReader gameReader() {
        return gameReader;
    }

    public GameInitialization gameInitialization() {
        return gameInitialization;
    }

    public GameNewTurn gameNewTurn() {
        return gameNewTurn;
    }

    public GameStrategy gameStrategy() {
        return gameStrategy;
    }

    @SuppressWarnings("unchecked")
    private static <T> T mock(Class<T> klass) {
        InvocationHandler handler = (proxy, method, args) -> null;
        Object proxy = Proxy.newProxyInstance(Application.class.getClassLoader(), new Class[]{ klass }, handler);
        return (T) proxy;
    }
}
