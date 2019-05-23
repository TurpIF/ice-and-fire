package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.TrainedUnit;
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
    private GameRepository gameRepository;

    @Mock
    private TrainedUnit trainedUnit;

    private TrainCommand command;

    @Before
    public void setUp() throws Exception {
        command = new TrainCommand(trainedUnit, cell, gameRepository);
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
    public void getCell_GivenCell_ReturnsIt() throws Exception {
        GameCell commandCell = command.getCell();

        assertThat(commandCell).isEqualTo(cell);
    }

    @Test
    public void getTrainUnit_GivenUnit_ReturnsIt() throws Exception {
        TrainedUnit commandTrainedUnit = command.getTrainedUnit();

        assertThat(commandTrainedUnit).isEqualTo(trainedUnit);
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
    public void isValid_GivenWallCell_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(cell.isWall()).thenReturn(true);

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
        when(cell.containsBeatableOpponentFor(trainedUnit)).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenNotEnoughGold_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(gameRepository.getPlayerGold()).thenReturn(10);
        when(trainedUnit.getTrainingCost()).thenReturn(11);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenCellProtectedByOpponentTowerAndUnitCantReachIt_ReturnsFalse() throws Exception {
        givenValidTrain();
        when(cell.isProtectedByOpponentTower()).thenReturn(true);
        when(trainedUnit.canReachTower()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenCellProtectedByOpponentTowerAndUnitCanReachIt_ReturnsTrue() throws Exception {
        givenValidTrain();
        when(cell.isProtectedByOpponentTower()).thenReturn(true);
        when(trainedUnit.canReachTower()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

    @Test
    public void isValid_GivenCellUnprotectedByOpponentTower_ReturnsTrue() throws Exception {
        givenValidTrain();
        when(cell.isProtectedByOpponentTower()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

    private void givenValidTrain() {
        when(cell.containsAlly()).thenReturn(false);
        when(cell.isInMyTerritoryOrInItsNeighborhood()).thenReturn(true);
        when(gameRepository.getPlayerGold()).thenReturn(10);
        when(trainedUnit.getTrainingCost()).thenReturn(10);
        when(cell.containsBeatableOpponentFor(trainedUnit)).thenReturn(true);
        when(cell.isWall()).thenReturn(false);
        when(cell.isProtectedByOpponentTower()).thenReturn(false);
        when(trainedUnit.canReachTower()).thenReturn(true);
    }

    private void givenLevel(int level) {
        when(trainedUnit.getLevel()).thenReturn(level);
    }

}