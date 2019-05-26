package fr.pturpin.hackathon.iceandfire.command;

import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.TrainedPlayerBuilding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class BuildCommand_UT {

    @Mock
    private GameCell cell;

    @Mock
    private TrainedPlayerBuilding trainedPlayerBuilding;

    @Mock
    private GameRepository gameRepository;

    private BuildCommand command;

    @Before
    public void setUp() throws Exception {
        command = new BuildCommand(trainedPlayerBuilding, cell, gameRepository);
    }

    @Test
    public void getFormattedCommand_GivenBuildingTypeAndPosition_ReturnsCommand() throws Exception {
        givenPosition(1, 2);
        givenBuildingType(BuildingType.MINE);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("BUILD MINE 1 2");
    }

    @Test
    public void getFormattedCommand_GivenBuildingTypeAndPosition_ReturnsCommand2() throws Exception {
        givenPosition(2, 3);
        givenBuildingType(BuildingType.MINE);

        String formattedCommand = command.getFormattedCommand();

        assertThat(formattedCommand).isEqualTo("BUILD MINE 2 3");
    }

    @Test
    public void isValid_GivenAllConditions_ReturnsTrue() throws Exception {
        givenValidConditions();

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

    @Test
    public void isValid_GivenQG_ReturnsFalse() throws Exception {
        givenValidConditions();
        givenBuildingType(BuildingType.QG);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenMineAndCellNotOnMineSpot_ReturnsFalse() throws Exception {
        givenValidConditions();
        givenBuildingType(BuildingType.MINE);
        when(cell.isMineSpot()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenTowerAndCellOnMineSpot_ReturnsFalse() throws Exception {
        givenValidConditions();
        givenBuildingType(BuildingType.TOWER);
        when(cell.isMineSpot()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenTowerAndCellNotOnMineSpot_ReturnsTrue() throws Exception {
        givenValidConditions();
        givenBuildingType(BuildingType.TOWER);
        when(cell.isMineSpot()).thenReturn(false);

        boolean valid = command.isValid();

        assertThat(valid).isTrue();
    }

    @Test
    public void isValid_GivenOccupiedCell_ReturnsFalse() throws Exception {
        givenValidConditions();
        when(cell.isOccupied()).thenReturn(true);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    @Test
    public void isValid_GivenNotEnoughGold_ReturnsFalse() throws Exception {
        givenValidConditions();
        when(trainedPlayerBuilding.getCost()).thenReturn(10);
        when(gameRepository.getPlayerGold()).thenReturn(9);

        boolean valid = command.isValid();

        assertThat(valid).isFalse();
    }

    private void givenValidConditions() {
        givenBuildingType(BuildingType.MINE);
        when(cell.isMineSpot()).thenReturn(true);
        when(trainedPlayerBuilding.getCost()).thenReturn(10);
        when(gameRepository.getPlayerGold()).thenReturn(10);
        when(cell.isOccupied()).thenReturn(false);
    }

    @Test
    public void execute_GivenRepository_InvokesTheBuilding() throws Exception {
        command.execute();

        verify(gameRepository).invokeNewBuilding(trainedPlayerBuilding, cell);
    }

    private void givenPosition(int x, int y) {
        when(cell.getPosition()).thenReturn(new Position(x, y));
    }

    private void givenBuildingType(BuildingType buildingType) {
        when(trainedPlayerBuilding.getBuildingType()).thenReturn(buildingType);
    }

}