package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OpponentBuilding_UT {

    private Position position;

    @Test
    public void getType_GivenType_ReturnsIt() throws Exception {
        OpponentBuilding building = new OpponentBuilding(BuildingType.QG, position);

        BuildingType type = building.getType();

        assertThat(type).isEqualTo(BuildingType.QG);
    }

}