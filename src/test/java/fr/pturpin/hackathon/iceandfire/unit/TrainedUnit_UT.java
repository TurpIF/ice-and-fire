package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class TrainedUnit_UT {

    @Mock
    private TrainedUnit otherUnit;

    @Mock
    private OpponentBuilding opponentBuilding;

    private TrainedUnit unit;

    @Before
    public void setUp() throws Exception {
        unit = new TrainedUnit(1);
    }

    @Test
    public void getLevel_GivenLevel_ReturnsIt() throws Exception {
        int level = unit.getLevel();

        assertThat(level).isEqualTo(1);
    }

    @Test
    public void getTrainingCost_GivenLevel1_Returns10() throws Exception {
        givenLevel(1);

        int cost = unit.getTrainingCost();

        assertThat(cost).isEqualTo(10);
    }

    @Test
    public void getTrainingCost_GivenLevel2_Returns20() throws Exception {
        givenLevel(2);

        int cost = unit.getTrainingCost();

        assertThat(cost).isEqualTo(20);
    }

    @Test
    public void getTrainingCost_GivenLevel3_Returns30() throws Exception {
        givenLevel(3);

        int cost = unit.getTrainingCost();

        assertThat(cost).isEqualTo(30);
    }

    @Test
    public void canBeat_GivenOpponentUnit_UseItsTrainedUnitRepresentation() throws Exception {
        unit = spy(unit);
        OpponentUnit opponentUnit = mock(OpponentUnit.class);

        when(opponentUnit.asTrainedUnit()).thenReturn(otherUnit);
        doReturn(true).when(unit).canBeat(otherUnit);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isEqualTo(true);
    }

    @Test
    public void canBeat_GivenPlayerUnit_UseItsTrainedUnitRepresentation() throws Exception {
        unit = spy(unit);
        PlayerUnit playerUnit = mock(PlayerUnit.class);

        when(playerUnit.asTrainedUnit()).thenReturn(otherUnit);
        doReturn(true).when(unit).canBeat(otherUnit);

        boolean canBeat = unit.canBeat(playerUnit);

        assertThat(canBeat).isEqualTo(true);
    }

    @Test
    public void canBeat_GivenLevel1AgainstAnyLevel_ReturnsFalse() throws Exception {
        givenLevel(1);
        when(otherUnit.getLevel()).thenReturn(1);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenLevel1AgainstAnyLevel_ReturnsFalse2() throws Exception {
        givenLevel(1);
        when(otherUnit.getLevel()).thenReturn(2);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenLevel2AgainstLevel1_ReturnsTrue() throws Exception {
        givenLevel(2);
        when(otherUnit.getLevel()).thenReturn(1);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenLevel2AgainstLevelAbove1_ReturnsFalse() throws Exception {
        givenLevel(2);
        when(otherUnit.getLevel()).thenReturn(2);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenLevel3AgainstAnyLevel_ReturnsTrue() throws Exception {
        givenLevel(3);
        when(otherUnit.getLevel()).thenReturn(1);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenLevel3AgainstAnyLevel_ReturnsTrue2() throws Exception {
        givenLevel(3);
        when(otherUnit.getLevel()).thenReturn(3);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenAnyLevelAndAnyBuilding_ReturnsTrue() throws Exception {
        givenLevel(1);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isTrue();
    }

    private void givenLevel(int level) {
        unit = new TrainedUnit(level);
    }

}