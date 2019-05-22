package fr.pturpin.hackathon.iceandfire.game;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.GameCell;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.unit.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        CellType[] grid = getFullGrid();
        grid[4 * 12 + 6] = CellType.INACTIVE_THEIR;

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
    public void getOpponentBuildingAt_GivenUpdateWithNoOpponentBuilding_ReturnsEmpty() throws Exception {
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, position);
        game.onNewTurn().setBuildingCount(1);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, position);

        Optional<OpponentBuilding> opponentBuilding = game.getOpponentBuildingAt(position);

        assertThat(opponentBuilding).isEmpty();
    }

    private CellType[] getFullGrid() {
        CellType[] grid = new CellType[12 * 12];
        Arrays.fill(grid, CellType.NIL);
        return grid;
    }

}