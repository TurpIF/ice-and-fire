package fr.pturpin.hackathon.iceandfire.cell;

import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class GameCell_UT {

    @Mock
    private GameRepository gameRepository;

    private Position position;

    private GameCell cell;

    @Mock
    private PlayerUnit playerUnit;

    @Mock
    private PlayerBuilding playerBuilding;

    @Mock
    private TrainedUnit trainedUnit;

    @Mock
    private OpponentUnit opponentUnit;

    @Mock
    private OpponentBuilding opponentBuilding;

    @Before
    public void setUp() throws Exception {
        position = new Position(0, 0);
        cell = new GameCell(gameRepository, position);
    }

    @Test
    public void getPosition_GivenPosition_ReturnsIt() throws Exception {
        givenPosition(1, 1);

        Position position = cell.getPosition();

        assertThat(position).isEqualTo(new Position(1, 1));
    }

    @Test
    public void containsAlly_GivenGameWithAllyUnit_ReturnsTrue() throws Exception {
        when(gameRepository.getPlayerUnitAt(position)).thenReturn(Optional.of(mock(PlayerUnit.class)));

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isTrue();
    }

    @Test
    public void containsAlly_GivenGameWithAllyBuilding_ReturnsTrue() throws Exception {
        when(gameRepository.getPlayerBuildingAt(position)).thenReturn(Optional.of(mock(PlayerBuilding.class)));

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isTrue();
    }

    @Test
    public void containsAlly_GivenGameWithoutAlly_ReturnsFalse() throws Exception {
        when(gameRepository.getPlayerBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getPlayerUnitAt(position)).thenReturn(Optional.empty());

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isFalse();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithoutOpponent_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.empty());

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithBeatableOpponentUnit_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(playerUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithBeatableOpponentBuilding_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.empty());
        when(playerUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithUnbeatableOpponentUnit_ReturnsFalse() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(playerUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithoutOpponent_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.empty());

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithBeatableOpponentUnit_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(trainedUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithBeatableOpponentBuilding_ReturnsTrue() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.empty());
        when(trainedUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithUnbeatableOpponentUnit_ReturnsFalse() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(trainedUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithUnbeatableOpponentBuilding_ReturnsFalse() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(trainedUnit.canBeat(opponentBuilding)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void isInMyTerritory_GivenCaseTypeActiveOfMine_ReturnsTrue() throws Exception {
        when(gameRepository.getCellType(position)).thenReturn(CellType.ACTIVE_MINE);

        boolean isInMyTerritory = cell.isInMyTerritory();

        assertThat(isInMyTerritory).isTrue();
    }

    @Test
    public void isInMyTerritory_GivenCaseTypeNotActiveOfMine_ReturnsFalse() throws Exception {
        when(gameRepository.getCellType(position)).thenReturn(CellType.INACTIVE_MINE);

        boolean isInMyTerritory = cell.isInMyTerritory();

        assertThat(isInMyTerritory).isFalse();
    }

    @Test
    public void isInMyTerritoryOrInItsNeighborhood_GivenCellInTerritory_ReturnsTrue() throws Exception {
        when(gameRepository.getCellType(position)).thenReturn(CellType.ACTIVE_MINE);

        boolean inMyTerritoryOrInItsNeighborhood = cell.isInMyTerritoryOrInItsNeighborhood();

        assertThat(inMyTerritoryOrInItsNeighborhood).isTrue();
    }

    @Test
    public void isInMyTerritoryOrInItsNeighborhood_GivenAnyNeighborInTerritory_ReturnsTrue() throws Exception {
        GameCell cellNotInTerritory = mock(GameCell.class);
        GameCell cellInTerritory = mock(GameCell.class);

        when(cellInTerritory.isInMyTerritory()).thenReturn(true);
        when(cellNotInTerritory.isInMyTerritory()).thenReturn(false);

        Position anyNeighbor = position.getNeighbors().stream().skip(1).findAny().get();
        when(gameRepository.getCell(any())).thenAnswer(invocation -> {
            Position neighborPosition = invocation.getArgument(0);
            if (neighborPosition.equals(anyNeighbor)) {
                return cellInTerritory;
            }
            return cellNotInTerritory;
        });

        boolean inMyTerritoryOrInItsNeighborhood = cell.isInMyTerritoryOrInItsNeighborhood();

        assertThat(inMyTerritoryOrInItsNeighborhood).isTrue();
    }

    @Test
    public void isInMyTerritoryOrInItsNeighborhood_GivenNoNeighborInTerritory_ReturnsFalse() throws Exception {
        GameCell cellNotInTerritory = mock(GameCell.class);
        when(cellNotInTerritory.isInMyTerritory()).thenReturn(false);

        when(gameRepository.getCell(any())).thenReturn(cellNotInTerritory);

        boolean inMyTerritoryOrInItsNeighborhood = cell.isInMyTerritoryOrInItsNeighborhood();

        assertThat(inMyTerritoryOrInItsNeighborhood).isFalse();
    }

    @Test
    public void isWall_GivenNilCell_ReturnsTrue() {
        when(gameRepository.getCellType(position)).thenReturn(CellType.NIL);

        boolean wall = cell.isWall();

        assertThat(wall).isTrue();
    }

    @Test
    public void isWall_GivenNoNilCell_ReturnsFalse() {
        when(gameRepository.getCellType(position)).thenReturn(CellType.NEUTRAL);

        boolean wall = cell.isWall();

        assertThat(wall).isFalse();
    }

    @Test
    public void isMineSpot_GivenGameWithoutSpot_ReturnsFalse() throws Exception {
        when(gameRepository.isMineSpot(position)).thenReturn(false);

        boolean mineSpot = cell.isMineSpot();

        assertThat(mineSpot).isFalse();
    }

    @Test
    public void isMineSpot_GivenGameWithSpot_ReturnsTrue() throws Exception {
        when(gameRepository.isMineSpot(position)).thenReturn(true);

        boolean mineSpot = cell.isMineSpot();

        assertThat(mineSpot).isTrue();
    }

    @Test
    public void isOccupied_GivenGameWithNoUnitNorBuilding_ReturnsFalse() throws Exception {
        givenFreePosition();

        boolean occupied = cell.isOccupied();

        assertThat(occupied).isFalse();
    }

    @Test
    public void isOccupied_GivenGameWithPlayerUnit_ReturnsTrue() throws Exception {
        givenFreePosition();
        when(gameRepository.getPlayerUnitAt(position)).thenReturn(Optional.of(playerUnit));

        boolean occupied = cell.isOccupied();

        assertThat(occupied).isTrue();
    }

    @Test
    public void isOccupied_GivenGameWithPlayerBuilding_ReturnsTrue() throws Exception {
        givenFreePosition();
        when(gameRepository.getPlayerBuildingAt(position)).thenReturn(Optional.of(playerBuilding));

        boolean occupied = cell.isOccupied();

        assertThat(occupied).isTrue();
    }

    @Test
    public void isOccupied_GivenGameWithOpponentUnit_ReturnsTrue() throws Exception {
        givenFreePosition();
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));

        boolean occupied = cell.isOccupied();

        assertThat(occupied).isTrue();
    }

    @Test
    public void isOccupied_GivenGameWithOpponentBuilding_ReturnsTrue() throws Exception {
        givenFreePosition();
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));

        boolean occupied = cell.isOccupied();

        assertThat(occupied).isTrue();
    }

    @Test
    public void isProtectedByOpponentTower_GivenGameWithTowerOnAnyNeighborCells_ReturnsTrue() throws Exception {
        GameCell cellWithTower = mock(GameCell.class);
        GameCell cellWithoutTower = mock(GameCell.class);

        when(gameRepository.getCellType(position)).thenReturn(CellType.ACTIVE_THEIR);
        when(cellWithTower.containsTower()).thenReturn(true);
        when(cellWithoutTower.containsTower()).thenReturn(false);

        Position anyNeighbor = position.getNeighbors().stream().skip(1).findAny().get();
        when(gameRepository.getCell(any())).thenAnswer(invocation -> {
            Position neighborPosition = invocation.getArgument(0);
            if (neighborPosition.equals(anyNeighbor)) {
                return cellWithTower;
            }
            return cellWithoutTower;
        });

        boolean isProtected = cell.isProtectedByOpponentTower();

        assertThat(isProtected).isTrue();
    }

    @Test
    public void isProtectedByOpponentTower_GivenCellNotInOpponentTerritory_ReturnsFalse() throws Exception {
        when(gameRepository.getCellType(position)).thenReturn(CellType.INACTIVE_THEIR);

        boolean isProtected = cell.isProtectedByOpponentTower();

        assertThat(isProtected).isFalse();
    }

    @Test
    public void isProtectedByOpponentTower_GivenGameWithNoTowerOnAllNeighborCells_ReturnsFalse() throws Exception {
        GameCell cellWithoutTower = mock(GameCell.class);
        when(cellWithoutTower.containsTower()).thenReturn(false);

        when(gameRepository.getCellType(position)).thenReturn(CellType.ACTIVE_THEIR);
        when(gameRepository.getCell(any())).thenReturn(cellWithoutTower);

        boolean isProtected = cell.isProtectedByOpponentTower();

        assertThat(isProtected).isFalse();
    }

    @Test
    public void containsTower_GivenGameWithoutTower_ReturnsFalse() throws Exception {
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());

        boolean containsTower = cell.containsTower();

        assertThat(containsTower).isFalse();
    }

    @Test
    public void containsTower_GivenGameWithAnotherBuilding_ReturnsFalse() throws Exception {
        when(opponentBuilding.getType()).thenReturn(BuildingType.MINE);
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));

        boolean containsTower = cell.containsTower();

        assertThat(containsTower).isFalse();
    }

    @Test
    public void containsTower_GivenGameWithTower_ReturnsTrue() throws Exception {
        when(opponentBuilding.getType()).thenReturn(BuildingType.TOWER);
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));

        boolean containsTower = cell.containsTower();

        assertThat(containsTower).isTrue();
    }

    @Test
    public void setEnteringUnit_GivenGame_NotifyGameForNewPosition() throws Exception {
        cell.setEnteringUnit(playerUnit);

        verify(gameRepository).moveUnit(playerUnit, cell);
    }

    @Test
    public void removeLeavingUnit_DoesNothing() throws Exception {
        cell.removeLeavingUnit(playerUnit);

        verifyZeroInteractions(gameRepository);
    }

    @Test
    public void invokeNewUnit_GivenGame_InvokesIt() throws Exception {
        cell.invokeNewUnit(trainedUnit);

        verify(gameRepository).invokeNewUnit(trainedUnit, cell);
    }

    private void givenFreePosition() {
        when(gameRepository.getPlayerBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getPlayerBuildingAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentUnitAt(position)).thenReturn(Optional.empty());
        when(gameRepository.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
    }

    private void givenPosition(int x, int y) {
        position = new Position(x, y);
        cell = new GameCell(gameRepository, position);
    }

}