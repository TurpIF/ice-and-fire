package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OpponentUnit_UT {

    @Test
    public void getLevel_GivenLevel_ReturnsIt() throws Exception {
        OpponentUnit unit = new OpponentUnit(1);

        int level = unit.getLevel();

        assertThat(level).isEqualTo(1);
    }

}