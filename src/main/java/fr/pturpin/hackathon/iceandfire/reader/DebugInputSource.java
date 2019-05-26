package fr.pturpin.hackathon.iceandfire.reader;

import java.io.PrintStream;

public class DebugInputSource implements GameInputSource {

    private final GameInputSource source;
    private final PrintStream out;

    public DebugInputSource(GameInputSource source, PrintStream out) {
        this.source = source;
        this.out = out;
    }

    @Override
    public int nextInt() {
        int value = source.nextInt();
        out.print(value);
        out.print(' ');
        return value;
    }

    @Override
    public String nextLine() {
        String line = source.nextLine();
        out.println(line);
        return line;
    }

}
