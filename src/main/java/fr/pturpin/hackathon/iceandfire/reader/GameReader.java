package fr.pturpin.hackathon.iceandfire.reader;

import fr.pturpin.hackathon.iceandfire.cell.CellType;
import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.game.GameInitialization;
import fr.pturpin.hackathon.iceandfire.game.GameNewTurn;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.Owner;

public class GameReader {

    private final GameInputSource inputSource;

    public GameReader(GameInputSource inputSource) {
        this.inputSource = inputSource;
    }

    public void readInit(GameInitialization initialization) {
        int numberMineSpots = inputSource.nextInt();

        initialization.setMineSpotCount(numberMineSpots);

        for (int i = 0; i < numberMineSpots; i++) {
            Position position = readPosition();
            initialization.addMineSpot(position);
        }
    }

    private Position readPosition() {
        int x = inputSource.nextInt();
        int y = inputSource.nextInt();
        return new Position(x, y);
    }

    public void readNewTurn(GameNewTurn newTurn) {
        readGoldInfo(newTurn);
        readGrid(newTurn);
        readBuildings(newTurn);
        readUnits(newTurn);
    }

    private void readGoldInfo(GameNewTurn newTurn) {
        newTurn.setPlayerGold(inputSource.nextInt());
        newTurn.setPlayerRevenue(inputSource.nextInt());
        newTurn.setOpponentGold(inputSource.nextInt());
        newTurn.setOpponentRevenue(inputSource.nextInt());
    }

    private void readGrid(GameNewTurn newTurn) {
        CellType[] grid = new CellType[12 * 12];
        for (int i = 0; i < 12; i++) {
            String row = inputSource.nextLine();
            for (int j = 0; j < 12; j++) {
                int cellIndex = i * 12 + j;
                CellType cellType = readCellType(row.charAt(j));
                grid[cellIndex] = cellType;
            }
        }

        newTurn.setGrid(grid);
    }

    private CellType readCellType(char cellChar) {
        switch (cellChar) {
            case '#':
                return CellType.NIL;
            case '.':
                return CellType.NEUTRAL;
            case 'O':
                return CellType.ACTIVE_MINE;
            case 'o':
                return CellType.INACTIVE_MINE;
            case 'X':
                return CellType.ACTIVE_THEIR;
            case 'x':
                return CellType.INACTIVE_THEIR;
            default:
                throw new IllegalArgumentException("Unknown cell type: `" + cellChar + "`");
        }
    }

    private void readBuildings(GameNewTurn newTurn) {
        int buildingCount = inputSource.nextInt();
        newTurn.setBuildingCount(buildingCount);

        for (int i = 0; i < buildingCount; i++) {
            Owner owner = readOwner();
            BuildingType buildingType = readBuildingType();
            Position position = readPosition();

            newTurn.addBuilding(owner, buildingType, position);
        }
    }

    private Owner readOwner() {
        int ownerId = inputSource.nextInt();
        switch (ownerId) {
            case 0:
                return Owner.ME;
            case 1:
                return Owner.OTHER;
            default:
                throw new IllegalArgumentException("Unknown owner id: `" + ownerId + "`");
        }
    }

    private BuildingType readBuildingType() {
        int buildingId = inputSource.nextInt();
        if (buildingId == 0) {
            return BuildingType.QG;
        }
        throw new IllegalArgumentException("Unknown building id: `" + buildingId + "`");
    }

    private void readUnits(GameNewTurn newTurn) {
        int unitCount = inputSource.nextInt();
        newTurn.setUnitCount(unitCount);

        for (int i = 0; i < unitCount; i++) {
            Owner owner = readOwner();
            int unitId = inputSource.nextInt();
            int level = inputSource.nextInt();
            Position position = readPosition();

            newTurn.addUnit(owner, unitId, level, position);
        }
    }
}
