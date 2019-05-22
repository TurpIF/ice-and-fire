package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class PlayerUnit_UT {

    @Mock
    private OpponentBuilding opponentBuilding;

    @Mock
    private OpponentUnit opponentUnit;

    @Mock
    private TrainedPlayerUnit trainedPlayerUnit;

    private PlayerUnit unit;

    @Before
    public void setUp() throws Exception {
        unit = new PlayerUnit(1, new Position(0, 0), trainedPlayerUnit);
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
        when(trainedPlayerUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenOpponentUnit_AskTheTrainedUnit2() throws Exception {
        when(trainedPlayerUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenOpponentBuilding_AskTheTrainedUnit() throws Exception {
        when(trainedPlayerUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenOpponentBuilding_AskTheTrainedUnit2() throws Exception {
        when(trainedPlayerUnit.canBeat(opponentBuilding)).thenReturn(false);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isFalse();
    }

    private void givenId(int id) {
        unit = new PlayerUnit(id, unit.getPosition(), trainedPlayerUnit);
    }

    private void givenPosition(Position position) {
        unit = new PlayerUnit(unit.getId(), position, trainedPlayerUnit);
    }

}