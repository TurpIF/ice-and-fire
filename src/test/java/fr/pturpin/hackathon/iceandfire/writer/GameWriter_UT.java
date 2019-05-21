package fr.pturpin.hackathon.iceandfire.writer;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameWriter_UT {

    private static final String ENCODING = StandardCharsets.UTF_8.name();

    private GameWriter writer;

    private ByteArrayOutputStream baos;

    @Before
    public void setUp() throws Exception {
        baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos, false, ENCODING);
        writer = new GameWriter(stream);
    }

    @Test
    public void add_GivenSingleCommand_PrintIt() throws Exception {
        GameCommand command = getGameCommand("CMD1");

        writer.startSequence();
        writer.add(command);
        writer.endSequence();

        String printed = getPrintedString();

        assertThat(printed).isEqualTo("CMD1\n");
    }

    @Test
    public void add_GivenManyCommands_PrintThemOnSingleLineSeparated() throws Exception {
        GameCommand command1 = getGameCommand("CMD1");
        GameCommand command2 = getGameCommand("CMD2");

        writer.startSequence();
        writer.add(command1);
        writer.add(command2);
        writer.endSequence();

        String printed = getPrintedString();

        assertThat(printed).isEqualTo("CMD1;CMD2\n");
    }

    @Test
    public void endSequence_GivenManySequences_PrintThemOnDifferentLines() throws Exception {
        GameCommand command1 = getGameCommand("CMD1");
        GameCommand command2 = getGameCommand("CMD2");

        writer.startSequence();
        writer.add(command1);
        writer.endSequence();

        writer.startSequence();
        writer.add(command2);
        writer.endSequence();

        String printed = getPrintedString();

        assertThat(printed).isEqualTo("CMD1\nCMD2\n");
    }

    private GameCommand getGameCommand(String formattedCommand) {
        GameCommand command = mock(GameCommand.class);
        when(command.getFormattedCommand()).thenReturn(formattedCommand);
        return command;
    }

    private String getPrintedString() throws UnsupportedEncodingException {
        return baos.toString(ENCODING);
    }

}