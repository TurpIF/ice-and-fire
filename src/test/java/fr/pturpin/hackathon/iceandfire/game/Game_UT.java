package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class Game_UT {

    private Game game;
    private Position position;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        position = new Position(3, 4);
    }

    @Test
    public void getPlayerGold_GivenGoldOnNewTurn_ReturnsIt() throws Exception {
        game.onNewTurn().setPlayerGold(1);
        int playerGold = game.getPlayerGold();

        assertThat(playerGold).isEqualTo(1);
    }

    @Test
    public void getCellType_GivenGrid_ReturnsCellAtGivenPosition() throws Exception {
        CellType[] grid = getFullGrid(CellType.NIL);
        grid[6 * 12 + 4] = CellType.INACTIVE_THEIR;

        game.onNewTurn().setGrid(grid);
        CellType cellType = game.getCellType(new Position(4, 6));

        assertThat(cellType).isEqualTo(CellType.INACTIVE_THEIR);
    }

    @Test
    public void getCell_GivenPosition_ReturnsCell() throws Exception {
        GameCell cell = game.getCell(position);

        assertThat(cell).isEqualTo(new GameCell(game, position));
    }

    @Test
    public void getPlayerUnitAt_GivenNoOwnedUnit_ReturnsEmpty() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 1, 1, position);
        Optional<PlayerUnit> unit = game.getPlayerUnitAt(position);

        assertThat(unit).isEmpty();
    }

    @Test
    public void getPlayerUnitAt_GivenOwnedUnit_ReturnsMovablePlayer() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 2, 1, position);
        Optional<PlayerUnit> playerUnit = game.getPlayerUnitAt(position);

        assertThat(playerUnit)
                .isNotEmpty()
                .hasValueSatisfying(unit -> {
                    assertThat(unit.getId()).isEqualTo(2);
                    assertThat(unit.getPosition()).isEqualTo(position);
                    assertThat(unit.getLevel()).isEqualTo(1);
                    assertThat(unit.canMove()).isTrue();
                });
    }

    @Test
    public void getPlayerUnitAt_GivenUpdateWithNoOwnedUnit_ReturnsEmpty() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 2, 1, position);
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 2, 1, position);

        Optional<PlayerUnit> unit = game.getPlayerUnitAt(position);

        assertThat(unit).isEmpty();
    }

    @Test
    public void getOpponentUnitAt_GivenNoOpponentUnit_ReturnsEmpty() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 1, 1, position);
        Optional<OpponentUnit> unit = game.getOpponentUnitAt(position);

        assertThat(unit).isEmpty();
    }

    @Test
    public void getOpponentUnitAt_GivenOpponentUnit_ReturnsUnit() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 2, 1, position);
        Optional<OpponentUnit> opponentUnit = game.getOpponentUnitAt(position);

        assertThat(opponentUnit)
                .isNotEmpty()
                .hasValueSatisfying(unit -> {
                    assertThat(unit.getLevel()).isEqualTo(1);
                });
    }

    @Test
    public void getOpponentUnitAt_GivenUpdateWithNoOpponentUnit_ReturnsEmpty() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 2, 1, position);
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 2, 1, position);

        Optional<OpponentUnit> unit = game.getOpponentUnitAt(position);

        assertThat(unit).isEmpty();
    }

    @Test
    public void getPlayerBuildingAt_GivenNoOwnedBuilding_ReturnsEmpty() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);

        Optional<PlayerBuilding> playerBuilding = game.getPlayerBuildingAt(position);

        assertThat(playerBuilding).isEmpty();
    }

    @Test
    public void getPlayerBuildingAt_GivenOwnedBuilding_ReturnsTheBuilding() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, position);

        Optional<PlayerBuilding> playerBuilding = game.getPlayerBuildingAt(position);

        assertThat(playerBuilding)
                .isNotEmpty()
                .hasValueSatisfying(building -> {
                    assertThat(building.getType()).isEqualTo(BuildingType.QG);
                });
    }

    @Test
    public void getPlayerBuildingAt_GivenOwnedBuilding_ReturnsTheBuilding2() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.MINE, position);

        Optional<PlayerBuilding> playerBuilding = game.getPlayerBuildingAt(position);

        assertThat(playerBuilding)
                .isNotEmpty()
                .hasValueSatisfying(building -> {
                    assertThat(building.getType()).isEqualTo(BuildingType.MINE);
                });
    }

    @Test
    public void getPlayerBuildingAt_GivenUpdateWithNoOwnedBuilding_ReturnsEmpty() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, position);
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);

        Optional<PlayerBuilding> playerBuilding = game.getPlayerBuildingAt(position);

        assertThat(playerBuilding).isEmpty();
    }

    @Test
    public void getOpponentBuildingAt_GivenNoOpponentBuilding_ReturnsEmpty() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, position);

        Optional<OpponentBuilding> opponentBuilding = game.getOpponentBuildingAt(position);

        assertThat(opponentBuilding).isEmpty();
    }

    @Test
    public void getOpponentBuildingAt_GivenOpponentBuilding_ReturnsTheBuilding() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);

        Optional<OpponentBuilding> opponentBuilding = game.getOpponentBuildingAt(position);

        assertThat(opponentBuilding)
                .isNotEmpty()
                .hasValueSatisfying(building -> {
                    assertThat(building.getType()).isEqualTo(BuildingType.QG);
                });
    }

    @Test
    public void getOpponentBuildingAt_GivenOpponentBuilding_ReturnsTheBuilding2() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.MINE, position);

        Optional<OpponentBuilding> opponentBuilding = game.getOpponentBuildingAt(position);

        assertThat(opponentBuilding)
                .isNotEmpty()
                .hasValueSatisfying(building -> {
                    assertThat(building.getType()).isEqualTo(BuildingType.MINE);
                });
    }

    @Test
    public void getOpponentBuildingAt_GivenUpdateWithNoOpponentBuilding_ReturnsEmpty() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, position);

        Optional<OpponentBuilding> opponentBuilding = game.getOpponentBuildingAt(position);

        assertThat(opponentBuilding).isEmpty();
    }

    @Test
    public void getPlayerMineCount_GivenNoMine_Returns0() throws Exception {
        int playerMineCount = game.getPlayerMineCount();

        assertThat(playerMineCount).isEqualTo(0);
    }

    @Test
    public void getPlayerMineCount_GivenSomeMines_ReturnsTheCount() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.MINE, new Position(0, 1));
        game.onNewTurn().setBuildingCount(4);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.MINE, new Position(0, 0));
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.MINE, new Position(1, 0));
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, new Position(2, 0));
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.MINE, new Position(3, 0));

        int playerMineCount = game.getPlayerMineCount();

        assertThat(playerMineCount).isEqualTo(2);
    }

    @Test
    public void isMineSpot_GivenNoSpotAtThePosition_ReturnsFalse() throws Exception {
        game.onInitialization().addMineSpot(new Position(0, 0));

        boolean mineSpot = game.isMineSpot(new Position(1, 0));

        assertThat(mineSpot).isFalse();
    }

    @Test
    public void isMineSpot_GivenSpotAtThePosition_ReturnsFalse() throws Exception {
        game.onInitialization().addMineSpot(new Position(1, 0));

        boolean mineSpot = game.isMineSpot(new Position(1, 0));

        assertThat(mineSpot).isTrue();
    }

    @Test
    public void moveUnit_GivenNewEmptyCell_PutUnitAtTheNewPosition() throws Exception {
        Position oldPosition = new Position(0, 0);
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 1, 1, oldPosition);

        GameCell oldCell = game.getCell(oldPosition);
        GameCell newCell = game.getCell(newPosition);

        PlayerUnit playerUnit = game.getPlayerUnitAt(oldPosition).get();

        game.moveUnit(playerUnit, newCell);

        assertMovedUnit(oldCell, newCell, playerUnit);
    }

    @Test
    public void moveUnit_GivenNewCellWithBuilding_RemovesBuilding() throws Exception {
        Position oldPosition = new Position(0, 0);
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 1, 3, oldPosition);
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.TOWER, newPosition);

        GameCell oldCell = game.getCell(oldPosition);
        GameCell newCell = game.getCell(newPosition);
        PlayerUnit playerUnit = game.getPlayerUnitAt(oldPosition).get();

        game.moveUnit(playerUnit, newCell);

        assertMovedUnit(oldCell, newCell, playerUnit);
    }

    @Test
    public void moveUnit_GivenNewCellWithUnit_RemovesUnit() throws Exception {
        Position oldPosition = new Position(0, 0);
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        game.onNewTurn().setUnitCount(2);
        game.onNewTurn().addUnit(Owner.ME, 1, 3, oldPosition);
        game.onNewTurn().addUnit(Owner.OTHER, 2, 2, newPosition);

        GameCell oldCell = game.getCell(oldPosition);
        GameCell newCell = game.getCell(newPosition);
        PlayerUnit playerUnit = game.getPlayerUnitAt(oldPosition).get();

        game.moveUnit(playerUnit, newCell);

        assertMovedUnit(oldCell, newCell, playerUnit);
    }

    @Test
    public void moveUnit_GivenNewCellConnectingInactiveCell_MakesThemActive() throws Exception {
        Position oldPosition = new Position(0, 0);
        Position newPosition = new Position(1, 0);

        CellType[] grid = getFullGrid(CellType.NEUTRAL);
        grid[toIndex(new Position(2, 0))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(2, 1))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(3, 0))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(3, 1))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(4, 2))] = CellType.INACTIVE_MINE;

        game.onNewTurn().setGrid(grid);
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 1, 3, oldPosition);

        GameCell newCell = game.getCell(newPosition);
        PlayerUnit playerUnit = game.getPlayerUnitAt(oldPosition).get();

        game.moveUnit(playerUnit, newCell);

        assertThat(game.getCellType(new Position(2, 0))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(2, 1))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(3, 0))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(3, 1))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(4, 2))).isEqualTo(CellType.INACTIVE_MINE);
    }

    @Test
    public void invokeNewUnit_GivenCellConnectingOpponentCells_MakesThenInactiveButKeepUnitsAndBuildings() throws Exception {
        CellType[] grid = getFullGrid(CellType.NEUTRAL);
        grid[toIndex(new Position(0, 0))] = CellType.ACTIVE_THEIR;
        grid[toIndex(new Position(1, 0))] = CellType.ACTIVE_THEIR;
        grid[toIndex(new Position(2, 0))] = CellType.ACTIVE_THEIR;
        grid[toIndex(new Position(3, 0))] = CellType.ACTIVE_THEIR;
        grid[toIndex(new Position(3, 1))] = CellType.ACTIVE_THEIR;

        game.onNewTurn().setGrid(grid);
        game.onNewTurn().setBuildingCount(2);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, new Position(0, 0));
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.TOWER, new Position(3, 0));
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 1, 1, new Position(2, 0));

        GameCell cell = game.getCell(new Position(1, 0));
        game.invokeNewUnit(new TrainedUnit(1), cell);

        assertThat(game.getCellType(new Position(1, 0))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(0, 0))).isEqualTo(CellType.ACTIVE_THEIR);
        assertThat(game.getCellType(new Position(2, 0))).isEqualTo(CellType.INACTIVE_THEIR);
        assertThat(game.getCellType(new Position(3, 0))).isEqualTo(CellType.INACTIVE_THEIR);
        assertThat(game.getCellType(new Position(3, 1))).isEqualTo(CellType.INACTIVE_THEIR);
        assertThat(game.getOpponentUnitAt(new Position(2, 0))).isNotEmpty();
        assertThat(game.getOpponentBuildingAt(new Position(3, 0))).isNotEmpty();
    }

    private int toIndex(Position position) {
        return position.getY() * 12 + position.getX();
    }

    private void assertMovedUnit(GameCell oldCell, GameCell newCell, PlayerUnit playerUnit) {
        Position oldPosition = oldCell.getPosition();
        Position newPosition = newCell.getPosition();

        assertThat(newCell.containsAlly()).isTrue();
        assertThat(newCell.isInMyTerritory()).isTrue();
        assertThat(newCell.isOccupied()).isTrue();
        assertThat(oldCell.containsAlly()).isFalse();
        assertThat(oldCell.isOccupied()).isFalse();
        assertThat(game.getPlayerUnitAt(oldPosition)).isEmpty();
        assertThat(game.getPlayerUnitAt(newPosition)).hasValue(playerUnit);
        assertThat(game.getCellType(newPosition)).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getOpponentUnitAt(newPosition)).isEmpty();
        assertThat(game.getOpponentBuildingAt(newPosition)).isEmpty();
    }

    @Test
    public void invokeNewUnit_GivenPlayerGold_RemoveTrainingCost() throws Exception {
        GameCell newCell = game.getCell(new Position(1, 0));
        TrainedUnit trainedUnit = spy(new TrainedUnit(3));

        game.onNewTurn().setPlayerGold(10);
        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        when(trainedUnit.getTrainingCost()).thenReturn(7);

        game.invokeNewUnit(trainedUnit, newCell);

        int playerGold = game.getPlayerGold();

        assertThat(playerGold).isEqualTo(3);
    }

    @Test
    public void invokeNewUnit_GivenNewFreeCell_InvokeNewUnitAtGivenPosition() throws Exception {
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));

        GameCell newCell = game.getCell(newPosition);
        TrainedUnit trainedUnit = new TrainedUnit(3);

        game.invokeNewUnit(trainedUnit, newCell);

        assertInvokedUnit(newCell, trainedUnit);
    }

    @Test
    public void invokeNewUnit_GivenNewCellWithUnit_RemovesUnit() throws Exception {
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 1, 1, newPosition);

        GameCell newCell = game.getCell(newPosition);
        TrainedUnit trainedUnit = new TrainedUnit(3);

        game.invokeNewUnit(trainedUnit, newCell);

        assertInvokedUnit(newCell, trainedUnit);
    }

    @Test
    public void invokeNewUnit_GivenNewCellWithBuilding_RemovesBuilding() throws Exception {
        Position newPosition = new Position(1, 0);

        game.onNewTurn().setGrid(getFullGrid(CellType.NEUTRAL));
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.OTHER, 1, 1, newPosition);

        GameCell newCell = game.getCell(newPosition);
        TrainedUnit trainedUnit = new TrainedUnit(3);

        game.invokeNewUnit(trainedUnit, newCell);

        assertInvokedUnit(newCell, trainedUnit);
    }

    @Test
    public void invokeNewUnit_GivenNewCellConnectingInactiveCell_MakesThemActive() throws Exception {
        Position newPosition = new Position(1, 0);

        CellType[] grid = getFullGrid(CellType.NEUTRAL);
        grid[toIndex(new Position(2, 0))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(2, 1))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(3, 0))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(3, 1))] = CellType.INACTIVE_MINE;
        grid[toIndex(new Position(4, 2))] = CellType.INACTIVE_MINE;

        game.onNewTurn().setGrid(grid);

        GameCell newCell = game.getCell(newPosition);
        TrainedUnit trainedUnit = new TrainedUnit(3);

        game.invokeNewUnit(trainedUnit, newCell);

        assertThat(game.getCellType(new Position(2, 0))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(2, 1))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(3, 0))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(3, 1))).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getCellType(new Position(4, 2))).isEqualTo(CellType.INACTIVE_MINE);
    }

    private void assertInvokedUnit(GameCell newCell, TrainedUnit trainedUnit) {
        Position newPosition = newCell.getPosition();

        assertThat(newCell.containsAlly()).isTrue();
        assertThat(newCell.isInMyTerritory()).isTrue();
        assertThat(newCell.isOccupied()).isTrue();
        assertThat(game.getPlayerUnitAt(newPosition)).hasValueSatisfying(playerUnit-> {
            assertThat(playerUnit.getPosition()).isEqualTo(newPosition);
            assertThat(playerUnit.getLevel()).isEqualTo(trainedUnit.getLevel());
            assertThat(playerUnit.canMove()).isFalse();
        });
        assertThat(game.getCellType(newPosition)).isEqualTo(CellType.ACTIVE_MINE);
        assertThat(game.getOpponentUnitAt(newPosition)).isEmpty();
        assertThat(game.getOpponentBuildingAt(newPosition)).isEmpty();
    }

    @Test
    public void getAllCells_ReturnsAllCells() throws Exception {
        Stream<GameCell> allCells = game.getAllCells();

        assertThat(allCells).hasSize(12 * 12);
    }

    @Test
    public void getAllPlayerUnits_GivenEmptyGame_ReturnsEmpy() throws Exception {
        game.onNewTurn().setUnitCount(0);

        Stream<PlayerUnit> allPlayerUnits = game.getAllPlayerUnits();

        assertThat(allPlayerUnits).isEmpty();
    }

    @Test
    public void getAllPlayerUnits_GivenSomeUnits_ReturnsThem() throws Exception {
        game.onNewTurn().setUnitCount(1);
        game.onNewTurn().addUnit(Owner.ME, 1, 1, new Position(0, 0));
        game.onNewTurn().setUnitCount(3);
        game.onNewTurn().addUnit(Owner.ME, 2, 1, new Position(1, 0));
        game.onNewTurn().addUnit(Owner.OTHER, 3, 1, new Position(2, 0));
        game.onNewTurn().addUnit(Owner.ME, 4, 1, new Position(3, 0));

        Stream<PlayerUnit> allPlayerUnits = game.getAllPlayerUnits();

        assertThat(allPlayerUnits)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        game.getPlayerUnitAt(new Position(1, 0)).get(),
                        game.getPlayerUnitAt(new Position(3, 0)).get());
    }

    @Test
    public void getOpponentQg_GivenQg_ReturnsIt() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);

        OpponentBuilding opponentQg = game.getOpponentQg();

        assertThat(opponentQg.getType()).isEqualTo(BuildingType.QG);
        assertThat(opponentQg.getPosition()).isEqualTo(position);
    }

    @Test
    public void invokeNewBuilding_GivenBuildingToInvoke_MakeNewBuildingAppear() throws Exception {
        TrainedPlayerBuilding trainedBuilding = mock(TrainedPlayerBuilding.class);
        Position position = new Position(5, 5);

        when(trainedBuilding.getBuildingType()).thenReturn(BuildingType.TOWER);
        when(trainedBuilding.getCost()).thenReturn(6);

        GameCell cell = game.getCell(position);
        game.onNewTurn().setPlayerGold(10);
        game.invokeNewBuilding(trainedBuilding, cell);

        assertThat(game.getPlayerBuildingAt(position))
                .isNotEmpty()
                .hasValueSatisfying(building -> assertThat(building.getType()).isEqualTo(BuildingType.TOWER));
        assertThat(game.getPlayerGold()).isEqualTo(4);
    }

    private CellType[] getFullGrid(CellType cellType) {
        CellType[] grid = new CellType[12 * 12];
        Arrays.fill(grid, cellType);
        return grid;
    }

}