package fr.pturpin.hackathon.iceandfire.reader;

import java.io.StringReader;
import java.util.Scanner;

public class GameInputScanner implements GameInputSource {

    private final Scanner scanner;

    private GameInputScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public static GameInputScanner fromString(String source) {
        StringReader reader = new StringReader(source);
        Scanner scanner = new Scanner(reader);
        return new GameInputScanner(scanner);
    }

    @Override
    public int nextInt() {
        return scanner.nextInt();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}
