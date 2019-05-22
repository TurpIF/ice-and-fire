package fr.pturpin.hackathon.iceandfire.unit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerBuilding_UT {

    @Test
    public void getType_GivenType_ReturnsIt() throws Exception {
        PlayerBuilding building = new PlayerBuilding(BuildingType.QG);

        BuildingType type = building.getType();

        assertThat(type).isEqualTo(BuildingType.QG);
    }

}