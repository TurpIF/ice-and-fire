package fr.pturpin.hackathon.iceandfire.command;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCommand_UT {

    @Test
    public void getFormattedCommand_GivenMessage_ReturnsCommand() throws Exception {
        MessageCommand command = new MessageCommand("my message");

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MSG my message");
    }

    @Test
    public void getFormattedCommand_GivenMessage_ReturnsCommand2() throws Exception {
        MessageCommand command = new MessageCommand("dummy");

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MSG dummy");
    }

    @Test
    public void isValid_GivenAnyGame_ReturnsTrue() throws Exception {
        MessageCommand command = new MessageCommand("dummy");

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

}