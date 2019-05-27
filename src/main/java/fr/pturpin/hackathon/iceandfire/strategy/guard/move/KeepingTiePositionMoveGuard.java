package fr.pturpin.hackathon.iceandfire.strategy.guard.move;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.MoveCommand;
import fr.pturpin.hackathon.iceandfire.game.GameRepository;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.OpponentUnit;
import fr.pturpin.hackathon.iceandfire.unit.PlayerUnit;

import java.util.Collection;
import java.util.Optional;

public class KeepingTiePositionMoveGuard implements MoveGuard {

    private final GameRepository game;

    public KeepingTiePositionMoveGuard(GameRepository game) {
        this.game = game;
    }

    @Override
    public boolean isUseless(MoveCommand command) {
        PlayerUnit playerUnit = command.getPlayerUnit();
        Position position = playerUnit.getPosition();
        Collection<Position> neighbors = position.getNeighbors();

        // If I'm covered by a turret, I can skip this rule
        for (Position neighbor : neighbors) {
            boolean isProtected = game.getPlayerBuildingAt(neighbor)
                    .filter(building -> building.getType() == BuildingType.TOWER)
                    .isPresent();

            if (isProtected) {
                return false;
            }
        }

        // If I'm next to an opponent that can't beat me and that I can't beat, then stay.
        for (Position neighbor : neighbors) {
            Optional<OpponentUnit> optOpponentUnit = game.getOpponentUnitAt(neighbor);
            if (optOpponentUnit.isPresent()) {
                OpponentUnit opponentUnit = optOpponentUnit.get();
                if (isTie(playerUnit, opponentUnit)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isTie(PlayerUnit playerUnit, OpponentUnit opponentUnit) {
        return !playerUnit.canBeat(opponentUnit) && !opponentUnit.canBeat(playerUnit);
    }
}
