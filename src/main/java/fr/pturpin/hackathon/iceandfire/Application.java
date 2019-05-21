package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.command.WaitCommand;
import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Application application = new Application();

        GameReader gameReader = application.getGameReader();
        GameWriter gameWriter = application.getGameWriter();

        GameInitialization initialization = application.getGameInitialization();
        GameNewTurn newTurn = application.getGameNewTurn();

        gameReader.readInit(initialization);

        while (true) {
            gameReader.readNewTurn(newTurn);

            gameWriter.startSequence();
            gameWriter.add(new WaitCommand());
            gameWriter.endSequence();
        }
    }

    private GameWriter getGameWriter() {
        return new GameWriter(System.out);
    }

    private GameReader getGameReader() {
        GameInputScanner inputSource = new GameInputScanner(new Scanner(System.in));
        return new GameReader(inputSource);
    }

    private GameInitialization getGameInitialization() {
        return mock(GameInitialization.class);
    }

    private GameNewTurn getGameNewTurn() {
        return mock(GameNewTurn.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> T mock(Class<T> klass) {
        InvocationHandler handler = (proxy, method, args) -> null;
        Object proxy = Proxy.newProxyInstance(Application.class.getClassLoader(), new Class[]{ klass }, handler);
        return (T) proxy;
    }

}
