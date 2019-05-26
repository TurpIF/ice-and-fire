package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.OpponentBuilding;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;

public final class OpponentCount {

    private int level1Count;
    private int level2Count;
    private int level3Count;
    private int towerCount;
    private int mineCount;
    private int qgCount;
    int size;

    public void clear() {
        level1Count = 0;
        level2Count = 0;
        level3Count = 0;
        towerCount = 0;
        mineCount = 0;
        size = 0;
    }

    public void add(OpponentCount other) {
        level1Count += other.level1Count;
        level2Count += other.level2Count;
        level3Count += other.level3Count;
        towerCount += other.towerCount;
        mineCount += other.mineCount;
        size += other.size;
    }

    public void add(OpponentUnit unit) {
        switch (unit.getLevel()) {
            case 1:
                level1Count++;
                break;
            case 2:
                level2Count++;
                break;
            case 3:
                level3Count++;
                break;
        }
    }

    public void add(OpponentBuilding building) {
        if (building.getType() == BuildingType.TOWER) {
            towerCount++;
        } else if (building.getType() == BuildingType.MINE) {
            mineCount++;
        } else if (building.getType() == BuildingType.QG) {
            qgCount++;
        }
    }

    public int score(int level) {
        return -5 * level + score();
    }

    public int score() {
        int score = 0;
        score += level1Count * 2;
        score += level2Count * 5;
        score += level3Count * 8;
        score += towerCount;
        score += mineCount;
        score += size;
        score += qgCount * 1000;
        return score;
    }
}
