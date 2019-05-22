package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.command.WaitCommand;
import fr.pturpin.hackathon.iceandfire.game.GameConfiguration;
import fr.pturpin.hackathon.iceandfire.writer.GameWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class Application_UT {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GameConfiguration configuration;
    private Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(configuration);
    }

    @Test
    public void init_GivenConfiguration_InitializeFromReader() throws Exception {
        application.init();

        verify(configuration.gameReader()).readInit(configuration.gameInitialization());
    }

    @Test
    public void update_GivenConfiguration_UpdateFromReader() throws Exception {
        application.update();

        verify(configuration.gameReader()).readNewTurn(configuration.gameNewTurn());
    }

    @Test
    public void buildCommands_GivenConfiguration_BuildFromStrategy() throws Exception {
        Collection<GameCommand> commands = mock(Collection.class);
        when(configuration.gameStrategy().buildCommands()).thenReturn(commands);

        Collection<GameCommand> builtCommands = application.buildCommands();

        assertThat(builtCommands).isEqualTo(commands);
    }

    @Test
    public void printCommands_GivenSomeCommands_PrintThenThroughWriter() throws Exception {
        GameCommand command1 = mock(GameCommand.class);
        GameCommand command2 = mock(GameCommand.class);
        List<GameCommand> commands = Arrays.asList(command1, command2);

        application.printCommands(commands);

        GameWriter gameWriter = configuration.gameWriter();

        InOrder inOrder = inOrder(gameWriter);
        inOrder.verify(gameWriter).startSequence();
        inOrder.verify(gameWriter).add(command1);
        inOrder.verify(gameWriter).add(command2);
        inOrder.verify(gameWriter).endSequence();
    }

    @Test
    public void printCommands_GivenNoCommands_PrintWaitCommand() throws Exception {
        application.printCommands(Collections.emptyList());

        GameWriter gameWriter = configuration.gameWriter();

        InOrder inOrder = inOrder(gameWriter);
        inOrder.verify(gameWriter).startSequence();
        inOrder.verify(gameWriter).add(any(WaitCommand.class));
        inOrder.verify(gameWriter).endSequence();
    }

}