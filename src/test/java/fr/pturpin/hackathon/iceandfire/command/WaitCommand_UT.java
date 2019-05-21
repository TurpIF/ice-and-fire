package fr.pturpin.hackathon.iceandfire.command;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitCommand_UT {

    private WaitCommand command;

    @Before
    public void setUp() throws Exception {
        command = new WaitCommand();
    }

    @Test
    public void getFormattedCommand_ReturnsCommand() throws Exception {
        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("WAIT");
    }

    @Test
    public void isValid_ReturnsTrue() throws Exception {
        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

}