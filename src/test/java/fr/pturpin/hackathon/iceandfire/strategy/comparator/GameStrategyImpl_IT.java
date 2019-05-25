package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.game.Game;
import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.strategy.GameStrategyImpl;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.Owner;
import org.junit.Test;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStrategyImpl_IT {

    @Test
    public void test_GivenKillingOccasion_KillThemAll() throws Exception {
        String input = "29\n15\n15\n29\n" +
                "OOO#########\n" +
                "OOO###XXX..#\n" +
                "OOO##XXXX...\n" +
                "##O##XXXX...\n" +
                "##OO#XXXX...\n" +
                "#.OOOXXXXX..\n" +
                "OOOOOOOXXXX#\n" +
                "OOOOOOO#XX##\n" +
                "OOOOOOO##X##\n" +
                "..OOOOO##XXX\n" +
                "#.OOOO###XXX\n" +
                "#########XXX\n" +
                "0\n0";

        GameReader reader = new GameReader(new GameInputScanner(new Scanner(new StringReader(input))));
        Game game = new Game();

        reader.readNewTurn(game.onNewTurn());

        game.onNewTurn().setBuildingCount(5);
        game.onNewTurn().addBuilding(Owner.ME, BuildingType.QG, new Position(0, 0));
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.QG, new Position(11, 11));
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.TOWER, new Position(5, 5));
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.TOWER, new Position(7, 4));
        game.onNewTurn().addBuilding(Owner.OTHER, BuildingType.TOWER, new Position(7, 6));

        game.onNewTurn().setUnitCount(15);
        game.onNewTurn().addUnit(Owner.ME, 25, 1, new Position(0, 8));
        game.onNewTurn().addUnit(Owner.ME, 15, 1, new Position(2, 10));
        game.onNewTurn().addUnit(Owner.ME, 2, 1, new Position(5, 9));
        game.onNewTurn().addUnit(Owner.ME, 1, 1, new Position(4, 5));
        game.onNewTurn().addUnit(Owner.ME, 6, 1, new Position(6, 7));
        game.onNewTurn().addUnit(Owner.ME, 14, 1, new Position(5, 7));
        game.onNewTurn().addUnit(Owner.ME, 18, 2, new Position(5, 6));
        game.onNewTurn().addUnit(Owner.ME, 27, 3, new Position(6, 6));

        game.onNewTurn().addUnit(Owner.OTHER, 8, 1, new Position(5, 3));
        game.onNewTurn().addUnit(Owner.OTHER, 21, 1, new Position(6, 3));
        game.onNewTurn().addUnit(Owner.OTHER, 22, 1, new Position(6, 4));
        game.onNewTurn().addUnit(Owner.OTHER, 26, 1, new Position(7, 1));
        game.onNewTurn().addUnit(Owner.OTHER, 28, 1, new Position(8, 1));
        game.onNewTurn().addUnit(Owner.OTHER, 4, 1, new Position(8, 2));
        game.onNewTurn().addUnit(Owner.OTHER, 3, 1, new Position(10, 6));

        GameStrategyImpl gameStrategy = new GameStrategyImpl(game);
        Collection<GameCommand> commands = gameStrategy.buildCommands();

        List<String> strCommands = commands.stream()
                .map(GameCommand::getFormattedCommand)
                .collect(Collectors.toList());

        assertThat(strCommands).contains("MOVE 27 7 6");
    }

}