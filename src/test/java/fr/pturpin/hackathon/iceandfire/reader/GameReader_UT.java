package fr.pturpin.hackathon.iceandfire.reader;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameInitialization;
import fr.pturpin.hackathon.iceandfire.game.GameNewTurn;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.Owner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class GameReader_UT {

    private StubGameInputSource inputSource;

    @Mock
    private GameInitialization initialization;

    @Mock
    private GameNewTurn newTurn;

    private GameReader reader;
    private CellType[] expectedGrid;

    @Before
    public void setUp() throws Exception {
        inputSource = new StubGameInputSource();
        reader = new GameReader(inputSource);
        expectedGrid = new CellType[12 * 12];
    }

    @Test
    public void readInit_GivenNoMines_DoesNothing() throws Exception {
        givenNoMines();

        reader.readInit(initialization);

        InOrder inOrder = inOrder(initialization);
        inOrder.verify(initialization).setMineSpotCount(0);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void readInit_GivenOneMine_AddTheMine() throws Exception {
        givenOneMine(1, 2);

        reader.readInit(initialization);

        InOrder inOrder = inOrder(initialization);
        inOrder.verify(initialization).setMineSpotCount(1);
        inOrder.verify(initialization).addMineSpot(new Position(1, 2));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void readInit_GivenManyMines_AddTheMines() throws Exception {
        givenTwoMines(1, 2, 4, 5);

        reader.readInit(initialization);

        InOrder inOrder = inOrder(initialization);
        inOrder.verify(initialization).setMineSpotCount(2);
        inOrder.verify(initialization).addMineSpot(new Position(1, 2));
        inOrder.verify(initialization).addMineSpot(new Position(4, 5));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void readNewTurn_GivenNeutralGame_SetNeutralGame() throws Exception {
        givenEmptyGold();
        givenGridFullOf('.');
        givenNoBuildings();
        givenNoUnits();

        reader.readNewTurn(newTurn);

        Arrays.fill(expectedGrid, CellType.NEUTRAL);

        InOrder inOrder = inOrder(newTurn);
        inOrder.verify(newTurn).setPlayerGold(0);
        inOrder.verify(newTurn).setPlayerRevenue(0);
        inOrder.verify(newTurn).setOpponentGold(0);
        inOrder.verify(newTurn).setOpponentRevenue(0);
        inOrder.verify(newTurn).setGrid(expectedGrid);
        inOrder.verify(newTurn).setBuildingCount(0);
        inOrder.verify(newTurn).setUnitCount(0);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void readNewTurn_GivenGold_SetPlayersGoldAndRevenue() throws Exception {
        givenGold(1, 2, 3, 4);
        givenGridFullOf('.');
        givenNoBuildings();
        givenNoUnits();

        reader.readNewTurn(newTurn);

        verify(newTurn).setPlayerGold(1);
        verify(newTurn).setPlayerRevenue(2);
        verify(newTurn).setOpponentGold(3);
        verify(newTurn).setOpponentRevenue(4);
    }

    @Test
    public void readNewTurn_GivenNilGrid_SetNilGame() throws Exception {
        givenEmptyGold();
        givenGridFullOf('#');
        givenNoBuildings();
        givenNoUnits();

        reader.readNewTurn(newTurn);

        Arrays.fill(expectedGrid, CellType.NIL);

        verify(newTurn).setGrid(expectedGrid);
    }

    @Test
    public void readNewTurn_GivenGridPossessedByMeAndActive_SetGameGrid() throws Exception {
        givenEmptyGold();
        givenGridFullOf('O');
        givenNoBuildings();
        givenNoUnits();

        reader.readNewTurn(newTurn);

        Arrays.fill(expectedGrid, CellType.ACTIVE_MINE);

        verify(newTurn).setGrid(expectedGrid);
    }

    @Test
    public void readNewTurn_GivenAnyGrid_SetGameGrid() throws Exception {
        givenEmptyGold();

        inputSource.thenReturn(".#OoXx......");
        givenManyCellRow(11, getRowFullOf('.'));

        givenNoBuildings();
        givenNoUnits();

        reader.readNewTurn(newTurn);

        Arrays.fill(expectedGrid, CellType.NEUTRAL);
        expectedGrid[0] = CellType.NEUTRAL;
        expectedGrid[1] = CellType.NIL;
        expectedGrid[2] = CellType.ACTIVE_MINE;
        expectedGrid[3] = CellType.INACTIVE_MINE;
        expectedGrid[4] = CellType.ACTIVE_THEIR;
        expectedGrid[5] = CellType.INACTIVE_THEIR;

        verify(newTurn).setGrid(expectedGrid);
    }

    @Test
    public void readNewTurn_GivenBuildings_SetBuilding() throws Exception {
        givenEmptyGold();
        givenGridFullOf('.');

        inputSource.thenReturn(2);
        givenBuilding(0, 0, 1, 2);
        givenBuilding(1, 0, 4, 6);

        givenNoUnits();

        reader.readNewTurn(newTurn);

        verify(newTurn).setBuildingCount(2);
        verify(newTurn).addBuilding(Owner.ME, BuildingType.QG, new Position(1, 2));
        verify(newTurn).addBuilding(Owner.OTHER, BuildingType.QG, new Position(4, 6));
    }

    @Test
    public void readNewTurn_GivenMineBuildings_SetBuilding() throws Exception {
        givenEmptyGold();
        givenGridFullOf('.');

        inputSource.thenReturn(1);
        givenBuilding(0, 1, 1, 2);

        givenNoUnits();

        reader.readNewTurn(newTurn);

        verify(newTurn).setBuildingCount(1);
        verify(newTurn).addBuilding(Owner.ME, BuildingType.MINE, new Position(1, 2));
    }

    @Test
    public void readNewTurn_GivenTowerBuildings_SetBuilding() throws Exception {
        givenEmptyGold();
        givenGridFullOf('.');

        inputSource.thenReturn(1);
        givenBuilding(0, 2, 1, 2);

        givenNoUnits();

        reader.readNewTurn(newTurn);

        verify(newTurn).setBuildingCount(1);
        verify(newTurn).addBuilding(Owner.ME, BuildingType.TOWER, new Position(1, 2));
    }

    @Test
    public void readNewTurn_GivenUnits_SetUnits() throws Exception {
        givenEmptyGold();
        givenGridFullOf('.');
        givenNoBuildings();

        inputSource.thenReturn(2);

        givenUnits(0, 2, 1, 2, 3);
        givenUnits(1, 3, 2, 4, 1);

        reader.readNewTurn(newTurn);

        verify(newTurn).setUnitCount(2);
        verify(newTurn).addUnit(Owner.ME, 2, 1, new Position(2, 3));
        verify(newTurn).addUnit(Owner.OTHER, 3, 2, new Position(4, 1));
    }

    @Test
    public void readInit_GivenRealSource_ConfigureInitialization() throws Exception {
        String input = "2\n" +
                "1 2\n" +
                "3 4";

        GameInputScanner source = GameInputScanner.fromString(input);
        GameReader reader = new GameReader(source);

        reader.readInit(initialization);

        verify(initialization).setMineSpotCount(2);
        verify(initialization).addMineSpot(new Position(1, 2));
        verify(initialization).addMineSpot(new Position(3, 4));
    }

    @Test
    public void readNewTurn_GivenRealSource_ConfigureNewTurn() throws Exception {
        StringBuilder sb = new StringBuilder();

        // Gold
        sb.append("1\n");
        sb.append("2\n");
        sb.append("3\n");
        sb.append("4\n");

        // Grid
        sb.append(".#OoXx......\n");
        for (int i = 0; i < 11; i++) {
            sb.append("............\n");
        }

        // Building
        sb.append("2\n");
        sb.append("0 0 1 2\n");
        sb.append("1 0 3 4\n");

        // Units
        sb.append("3\n");
        sb.append("0 1 1 2 3\n");
        sb.append("1 2 3 4 5\n");
        sb.append("1 3 2 6 7");

        GameInputScanner source = GameInputScanner.fromString(sb.toString());
        GameReader reader = new GameReader(source);

        reader.readNewTurn(newTurn);

        Arrays.fill(expectedGrid, CellType.NEUTRAL);
        expectedGrid[0] = CellType.NEUTRAL;
        expectedGrid[1] = CellType.NIL;
        expectedGrid[2] = CellType.ACTIVE_MINE;
        expectedGrid[3] = CellType.INACTIVE_MINE;
        expectedGrid[4] = CellType.ACTIVE_THEIR;
        expectedGrid[5] = CellType.INACTIVE_THEIR;

        verify(newTurn).setPlayerGold(1);
        verify(newTurn).setPlayerRevenue(2);
        verify(newTurn).setOpponentGold(3);
        verify(newTurn).setOpponentRevenue(4);
        verify(newTurn).setGrid(expectedGrid);
        verify(newTurn).setBuildingCount(2);
        verify(newTurn).addBuilding(Owner.ME, BuildingType.QG, new Position(1, 2));
        verify(newTurn).addBuilding(Owner.OTHER, BuildingType.QG, new Position(3, 4));
        verify(newTurn).setUnitCount(3);
        verify(newTurn).addUnit(Owner.ME, 1, 1, new Position(2, 3));
        verify(newTurn).addUnit(Owner.OTHER, 2, 3, new Position(4, 5));
        verify(newTurn).addUnit(Owner.OTHER, 3, 2, new Position(6, 7));
    }

    private void givenUnits(int owner, int unitId, int level, int x, int y) {
        inputSource.thenReturn(owner)
                .thenReturn(unitId)
                .thenReturn(level)
                .thenReturn(x)
                .thenReturn(y);
    }

    private void givenBuilding(int owner, int buildingType, int x, int y) {
        inputSource.thenReturn(owner)
                .thenReturn(buildingType)
                .thenReturn(x)
                .thenReturn(y);
    }

    private void givenNoMines() {
        inputSource.thenReturn(0);
    }

    private void givenOneMine(int x, int y) {
        inputSource.thenReturn(1)
                .thenReturn(x)
                .thenReturn(y);
    }

    private void givenTwoMines(int x1, int y1, int x2, int y2) {
        inputSource.thenReturn(2)
                .thenReturn(x1)
                .thenReturn(y1)
                .thenReturn(x2)
                .thenReturn(y2);
    }

    private void givenGold(int myGold, int myRevenue, int opponentGold, int opponentRevenue) {
        inputSource
                .thenReturn(myGold)
                .thenReturn(myRevenue)
                .thenReturn(opponentGold)
                .thenReturn(opponentRevenue);
    }

    private void givenEmptyGold() {
        givenGold(0, 0, 0, 0);
    }

    private void givenGridFullOf(char caseType) {
        String row = getRowFullOf(caseType);
        givenManyCellRow(12, row);
    }

    private String getRowFullOf(char caseType) {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(caseType);
        }
        return sb.toString();
    }

    private void givenManyCellRow(int rowCount, String row) {
        for (int i = 0; i < rowCount; i++) {
            inputSource.thenReturn(row);
        }
    }

    private void givenNoBuildings() {
        inputSource.thenReturn(0);
    }

    private void givenNoUnits() {
        inputSource.thenReturn(0);
    }

    private class StubGameInputSource implements GameInputSource {

        private ArrayList<Object> nextElements = new ArrayList<>();

        StubGameInputSource thenReturn(Object element) {
            nextElements.add(element);
            return this;
        }

        @Override
        public int nextInt() {
            return (Integer) nextElements.remove(0);
        }

        @Override
        public String nextLine() {
            return (String) nextElements.remove(0);
        }

    }
}