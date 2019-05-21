package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Position;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveCommand_UT {

    @Test
    public void getFormattedCommand_GivenIdAndPosition_ReturnsCommand() throws Exception {
        MoveCommand command = new MoveCommand(1, new Position(2, 3));

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MOVE 1 2 3");
    }

    @Test
    public void getFormattedCommand_GivenIdAndPosition_ReturnsCommand2() throws Exception {
        MoveCommand command = new MoveCommand(2, new Position(3, 4));

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("MOVE 2 3 4");
    }

}