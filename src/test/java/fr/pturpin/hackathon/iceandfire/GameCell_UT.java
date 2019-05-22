package fr.pturpin.hackathon.iceandfire;

import fr.pturpin.hackathon.iceandfire.unit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Strict.class)
public class GameCell_UT {

    @Mock
    private Game game;

    private Position position;

    private GameCell cell;

    @Mock
    private PlayerUnit playerUnit;

    @Mock
    private TrainedPlayerUnit trainedPlayerUnit;

    private OpponentUnit opponentUnit;
    private OpponentBuilding opponentBuilding;

    @Before
    public void setUp() throws Exception {
        position = new Position(0, 0);
        cell = new GameCell(game, position);
        opponentUnit = new OpponentUnit();
        opponentBuilding = new OpponentBuilding();
    }

    @Test
    public void getPosition_GivenPosition_ReturnsIt() throws Exception {
        givenPosition(1, 1);

        Position position = cell.getPosition();

        assertThat(position).isEqualTo(new Position(1, 1));
    }

    @Test
    public void containsAlly_GivenGameWithAllyUnit_ReturnsTrue() throws Exception {
        when(game.getPlayerUnitAt(position)).thenReturn(Optional.of(mock(PlayerUnit.class)));

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isTrue();
    }

    @Test
    public void containsAlly_GivenGameWithAllyBuilding_ReturnsTrue() throws Exception {
        when(game.getPlayerBuildingAt(position)).thenReturn(Optional.of(mock(PlayerBuilding.class)));

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isTrue();
    }

    @Test
    public void containsAlly_GivenGameWithoutAlly_ReturnsFalse() throws Exception {
        when(game.getPlayerBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getPlayerUnitAt(position)).thenReturn(Optional.empty());

        boolean containsAlly = cell.containsAlly();

        assertThat(containsAlly).isFalse();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithoutOpponent_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.empty());

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithBeatableOpponentUnit_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(playerUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithBeatableOpponentBuilding_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.empty());
        when(playerUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithUnbeatableOpponentUnit_ReturnsFalse() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(playerUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(playerUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithoutOpponent_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.empty());

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedPlayerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithBeatableOpponentUnit_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(trainedPlayerUnit.canBeat(opponentUnit)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedPlayerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithBeatableOpponentBuilding_ReturnsTrue() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.empty());
        when(trainedPlayerUnit.canBeat(opponentBuilding)).thenReturn(true);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedPlayerUnit);

        assertThat(containsBeatableOpponentFor).isTrue();
    }

    @Test
    public void containsBeatableOpponentForTrained_GivenGameWithUnbeatableOpponentUnit_ReturnsFalse() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.empty());
        when(game.getOpponentUnitAt(position)).thenReturn(Optional.of(opponentUnit));
        when(trainedPlayerUnit.canBeat(opponentUnit)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedPlayerUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void containsBeatableOpponentFor_GivenGameWithUnbeatableOpponentBuilding_ReturnsFalse() throws Exception {
        when(game.getOpponentBuildingAt(position)).thenReturn(Optional.of(opponentBuilding));
        when(trainedPlayerUnit.canBeat(opponentBuilding)).thenReturn(false);

        boolean containsBeatableOpponentFor = cell.containsBeatableOpponentFor(trainedPlayerUnit);

        assertThat(containsBeatableOpponentFor).isFalse();
    }

    @Test
    public void isInMyTerritory_GivenCaseTypeActiveOfMine_ReturnsTrue() throws Exception {
        when(game.getCellType(position)).thenReturn(CellType.ACTIVE_MINE);

        boolean isInMyTerritory = cell.isInMyTerritory();

        assertThat(isInMyTerritory).isTrue();
    }

    @Test
    public void isInMyTerritory_GivenCaseTypeNotActiveOfMine_ReturnsFalse() throws Exception {
        when(game.getCellType(position)).thenReturn(CellType.INACTIVE_MINE);

        boolean isInMyTerritory = cell.isInMyTerritory();

        assertThat(isInMyTerritory).isFalse();
    }

    @Test
    public void isInMyTerritoryOrInItsNeighborhood_GivenCellInTerritory_ReturnsTrue() throws Exception {
        when(game.getCellType(position)).thenReturn(CellType.ACTIVE_MINE);

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
        when(game.getCell(any())).thenAnswer(invocation -> {
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

        when(game.getCell(any())).thenReturn(cellNotInTerritory);

        boolean inMyTerritoryOrInItsNeighborhood = cell.isInMyTerritoryOrInItsNeighborhood();

        assertThat(inMyTerritoryOrInItsNeighborhood).isFalse();
    }

    private void givenPosition(int x, int y) {
        position = new Position(x, y);
        cell = new GameCell(game, position);
    }

}