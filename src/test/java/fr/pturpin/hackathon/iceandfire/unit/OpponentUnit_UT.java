package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class OpponentUnit_UT {

    @Mock
    private PlayerUnit playerUnit;

    @Mock
    private TrainedUnit trainedUnit;

    private OpponentUnit unit;

    @Before
    public void setUp() throws Exception {
        unit = new OpponentUnit(trainedUnit);
    }

    @Test
    public void getLevel_GivenLevel_ReturnsIt() throws Exception {
        when(trainedUnit.getLevel()).thenReturn(1);

        int level = unit.getLevel();

        assertThat(level).isEqualTo(1);
    }

    @Test
    public void asTrainedUnit_GivenTrainedUnit_ReturnsIt() throws Exception {
        TrainedUnit unitTrainedUnit = unit.asTrainedUnit();

        assertThat(unitTrainedUnit).isEqualTo(trainedUnit);
    }

    @Test
    public void canBeat_GivenBeatablePlayerUnit_ReturnsTrue() throws Exception {
        when(trainedUnit.canBeat(playerUnit)).thenReturn(true);

        boolean canBeat = unit.canBeat(playerUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenUnbeatablePlayerUnit_ReturnsFalse() throws Exception {
        when(trainedUnit.canBeat(playerUnit)).thenReturn(false);

        boolean canBeat = unit.canBeat(playerUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenBeatableUnit_ReturnsTrue() throws Exception {
        TrainedUnit otherUnit = mock(TrainedUnit.class);

        when(trainedUnit.canBeat(otherUnit)).thenReturn(true);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenUnbeatableUnit_ReturnsFalse() throws Exception {
        TrainedUnit otherUnit = mock(TrainedUnit.class);

        when(trainedUnit.canBeat(otherUnit)).thenReturn(false);

        boolean canBeat = unit.canBeat(otherUnit);

        assertThat(canBeat).isFalse();
    }

}