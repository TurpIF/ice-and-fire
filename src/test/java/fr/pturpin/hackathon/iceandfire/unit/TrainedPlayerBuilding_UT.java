package fr.pturpin.hackathon.iceandfire.unit;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class TrainedPlayerBuilding_UT {

    private TrainedPlayerBuilding building;

    @Mock
    private GameRepository gameRepository;

    @Before
    public void setUp() throws Exception {
        building = new TrainedPlayerBuilding(BuildingType.MINE, gameRepository);
    }

    @Test
    public void getBuildingType_GivenType_ReturnsIt() throws Exception {
        givenMine();

        BuildingType buildingType = building.getBuildingType();

        assertThat(buildingType).isEqualTo(BuildingType.MINE);
    }

    @Test
    public void getBuildingType_GivenType_ReturnsIt2() throws Exception {
        givenTower();

        BuildingType buildingType = building.getBuildingType();

        assertThat(buildingType).isEqualTo(BuildingType.TOWER);
    }

    @Test
    public void getCost_GivenFirstMine_ReturnsBaseCost() throws Exception {
        givenMine();
        when(gameRepository.getPlayerMineCount()).thenReturn(0);

        int cost = building.getCost();

        assertThat(cost).isEqualTo(20);
    }

    @Test
    public void getCost_GivenSecondMine_ReturnsComputedCost() throws Exception {
        givenMine();
        when(gameRepository.getPlayerMineCount()).thenReturn(1);

        int cost = building.getCost();

        assertThat(cost).isEqualTo(24);
    }

    @Test
    public void getCost_GivenNthMine_ReturnsComputedCost() throws Exception {
        givenMine();
        when(gameRepository.getPlayerMineCount()).thenReturn(4);

        int cost = building.getCost();

        assertThat(cost).isEqualTo(36);
    }

    @Test
    public void getCost_GivenTower_Returns15() throws Exception {
        givenTower();

        int cost = building.getCost();

        assertThat(cost).isEqualTo(15);
    }

    private void givenMine() {
        building = new TrainedPlayerBuilding(BuildingType.MINE, gameRepository);
    }

    private void givenTower() {
        building = new TrainedPlayerBuilding(BuildingType.TOWER, gameRepository);
    }

}