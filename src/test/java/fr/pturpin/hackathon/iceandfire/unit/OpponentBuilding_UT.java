package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OpponentBuilding_UT {

    @Test
    public void getType_GivenType_ReturnsIt() throws Exception {
        OpponentBuilding building = new OpponentBuilding(BuildingType.QG);

        BuildingType type = building.getType();

        assertThat(type).isEqualTo(BuildingType.QG);
    }

}