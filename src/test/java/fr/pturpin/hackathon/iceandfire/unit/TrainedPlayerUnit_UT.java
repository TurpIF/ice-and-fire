package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class TrainedPlayerUnit_UT {

    @Mock
    private OpponentUnit opponentUnit;

    @Mock
    private OpponentBuilding opponentBuilding;

    private TrainedPlayerUnit unit;

    @Before
    public void setUp() throws Exception {
        unit = new TrainedPlayerUnit(1);
    }

    @Test
    public void getLevel_GivenLevel_ReturnsIt() throws Exception {
        int level = unit.getLevel();

        assertThat(level).isEqualTo(1);
    }

    @Test
    public void canBeat_GivenLevel1AndOpponentUnitLevel1_ReturnsTrue() throws Exception {
        when(opponentUnit.getLevel()).thenReturn(1);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isTrue();
    }

    @Test
    public void canBeat_GivenLevel1AndOpponentUnitAboveLevel1_ReturnsFalse() throws Exception {
        when(opponentUnit.getLevel()).thenReturn(2);

        boolean canBeat = unit.canBeat(opponentUnit);

        assertThat(canBeat).isFalse();
    }

    @Test
    public void canBeat_GivenLevel1AndQG_ReturnsTrue() throws Exception {
        when(opponentBuilding.getType()).thenReturn(BuildingType.QG);

        boolean canBeat = unit.canBeat(opponentBuilding);

        assertThat(canBeat).isTrue();
    }

}