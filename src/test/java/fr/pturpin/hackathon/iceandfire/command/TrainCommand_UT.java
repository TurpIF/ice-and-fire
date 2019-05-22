package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.Game;
import fr.pturpin.hackathon.iceandfire.GameCell;
import fr.pturpin.hackathon.iceandfire.Position;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class TrainCommand_UT {

    @Mock
    private GameCell cell;

    @Mock
    private Game game;

    @Mock
    private TrainedPlayerUnit trainedPlayerUnit;

    private TrainCommand command;

    @Before
    public void setUp() throws Exception {
        command = new TrainCommand(trainedPlayerUnit, cell, game);
    }

    @Test
    public void getFormattedCommand_GivenLevelAndPosition_ReturnsCommand() throws Exception {
        when(cell.getPosition()).thenReturn(new Position(2, 3));
        givenLevel(1);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("TRAIN 1 2 3");
    }

    @Test
    public void getFormattedCommand_GivenLevelAndPosition_ReturnsCommand2() throws Exception {
        when(cell.getPosition()).thenReturn(new Position(3, 4));
        givenLevel(2);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("TRAIN 2 3 4");
    }

    @Test
    public void isValid_GivenValidTrain_ReturnsTrue() throws Exception {
        givenValidTrain();

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

    @Test
    public void isValid_GivenCellWithAlly_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(cell.containsAlly()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenCellNotInMyTerritoryNorInItsDirectNeighborhood_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(cell.isInMyTerritoryOrInItsNeighborhood()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenCellWithUnbeatableOpponent_ReturnsFalse() throws Exception {
        givenLevel(1);
        givenValidTrain();
        when(cell.containsBeatableOpponentFor(trainedPlayerUnit)).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenNotEnoughGold_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(game.getPlayerGold()).thenReturn(9);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    private void givenValidTrain() {
        when(cell.containsAlly()).thenReturn(false);
        when(cell.isInMyTerritoryOrInItsNeighborhood()).thenReturn(true);
        when(game.getPlayerGold()).thenReturn(10);
        when(cell.containsBeatableOpponentFor(trainedPlayerUnit)).thenReturn(true);
    }

    private void givenLevel(int level) {
        when(trainedPlayerUnit.getLevel()).thenReturn(level);
    }

}