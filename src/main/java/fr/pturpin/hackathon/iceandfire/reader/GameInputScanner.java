package fr.pturpin.hackathon.iceandfire.reader;

import java.io.StringReader;
import java.util.Scanner;

public class GameInputScanner implements GameInputSource {

    private final Scanner scanner;
    private boolean hasReadInt = false;

    public GameInputScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public static GameInputScanner fromString(String source) {
        StringReader reader = new StringReader(source);
        Scanner scanner = new Scanner(reader);
        return new GameInputScanner(scanner);
    }

    @Override
    public int nextInt() {
        hasReadInt = true;
        return scanner.nextInt();
    }

    @Override
    public String nextLine() {
        String line = scanner.nextLine();

        if (shouldConsumeNewLineAfterIntRead(line)) {
            line = scanner.nextLine();
        }
        hasReadInt = false;

        return line;
    }

    private boolean shouldConsumeNewLineAfterIntRead(String line) {
        return hasReadInt && line.isEmpty();
    }
}
