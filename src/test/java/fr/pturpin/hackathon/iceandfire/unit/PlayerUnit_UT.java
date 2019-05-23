package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class PlayerUnit_UT {

    @Mock
    private OpponentBuilding opponentBuilding;

    @Mock
    private OpponentUnit opponentUnit;

    @Mock
    private TrainedUnit trainedUnit;

    @Mock
    private GameCell cell;

    private PlayerUnit unit;

    @Before
    public void setUp() throws Exception {
        unit = new PlayerUnit(1, cell, trainedUnit);
    }

    @Test
    public void getId_GivenId_ReturnsIt() throws Exception {
        givenId(1);

        int id = unit.getId();

        assertThat(id).isEqualTo(1);
    }

    @Test
    public void canMove_GivenNewUnit_ReturnsTrue() throws Exception {
        boolean canMove = unit.canMove();

        assertThat(canMove).isTrue();
    }

    @Test
    public void getPosition_GivenPosition_ReturnsIt() throws Exception {
        Position position = new Position(2, 2);
        givenPosition(position);

        Position unitPosition = unit.getPosition();

        assertThat(unitPosition).isEqualTo(position);
    }

    @Test
    public void canBeat_GivenOpponentUnit_AskTheTrainedUnit() throws Exception {
        when(trainedUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenOpponentUnit_AskTheTrainedUnit2() throws Exception {
        when(trainedUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenOpponentBuilding_AskTheTrainedUnit() throws Exception {
        when(trainedUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenOpponentBuilding_AskTheTrainedUnit2() throws Exception {
        when(trainedUnit.canBeat(opponentBuilding)).thenReturn(false);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void asTrainedUnit_GivenOne_ReturnsIt() throws Exception {
        TrainedUnit unitTrainedUnit = unit.asTrainedUnit();

        assertThat(unitTrainedUnit).isEqualTo(trainedUnit);
    }

    @Test
    public void getLevel_GivenTrainedPlayerUnit_ReturnsItsLevel() throws Exception {
        when(trainedUnit.getLevel()).thenReturn(1);

        int level = unit.getLevel();

        assertThat(level).isEqualTo(1);
    }

    @Test
    public void canReachTower_GivenTrainedPlayer_AskTheTrainedUnit() throws Exception {
        when(trainedUnit.canReachTower()).thenReturn(true);

        boolean canReachTower = unit.canReachTower();

        assertThat(canReachTower).isTrue();
    }

    @Test
    public void moveOn_GivenCell_UpdateInternalAndNotifyOldAndNewCells() throws Exception {
        GameCell newCell = mock(GameCell.class);
        Position newPosition = new Position(1, 0);

        givenPosition(new Position(0, 0));
        when(newCell.getPosition()).thenReturn(newPosition);

        unit.moveOn(newCell);

        assertThat(unit.getPosition()).isEqualTo(newPosition);
        assertThat(unit.canMove()).isFalse();

        verify(newCell).setEnteringUnit(unit);
        verify(cell).removeLeavingUnit(unit);
    }

    private void givenId(int id) {
        unit = new PlayerUnit(id, cell, trainedUnit);
    }

    private void givenPosition(Position position) {
        when(cell.getPosition()).thenReturn(position);
    }

}