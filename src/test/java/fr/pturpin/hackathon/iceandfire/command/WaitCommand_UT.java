package fr.pturpin.hackathon.iceandfire.command;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitCommand_UT {

    @Test
    public void getFormattedCommand_ReturnsCommand() throws Exception {
        WaitCommand command = new WaitCommand();

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("WAIT");
    }

}