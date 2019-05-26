package fr.pturpin.hackathon.iceandfire.strategy.comparator;

import fr.pturpin.hackathon.iceandfire.cell.Position;
import fr.pturpin.hackathon.iceandfire.command.GameCommand;
import fr.pturpin.hackathon.iceandfire.game.Game;
import fr.pturpin.hackathon.iceandfire.reader.GameInputScanner;
import fr.pturpin.hackathon.iceandfire.reader.GameReader;
import fr.pturpin.hackathon.iceandfire.strategy.GameStrategyImpl;
import fr.pturpin.hackathon.iceandfire.unit.BuildingType;
import fr.pturpin.hackathon.iceandfire.unit.Owner;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStrategyImpl_IT {

    private Game game;
    private GameStrategyImpl gameStrategy;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        gameStrategy = new GameStrategyImpl(game);
    }

    @Test
    public void test_GivenKillingOccasion_KillThemAll() throws Exception {
        givenNewTurnInput("29\n15\n15\n29\n" +
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
                "0\n0");

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

        Collection<GameCommand> commands = gameStrategy.buildCommands();
        List<String> strCommands = getStrCommands(commands);

        assertThat(strCommands).contains("MOVE 27 7 6");
    }

    @Test
    public void test_GivenDefensiveOccasion_TrainDefensiveUnit() throws Exception {
        givenNewTurnInput("15\n14\n5\n16\n" +
                "OOO#####.###\n" +
                "OOO##....###\n" +
                "OOOO....####\n" +
                "###OO..#####\n" +
                "##.OOOX##..#\n" +
                "#..OOOX....#\n" +
                "#....OXX...#\n" +
                "#..##XXXX.##\n" +
                "#####XXXX###\n" +
                "####....XXXX\n" +
                "###....##XXX\n" +
                "###.#####XXX\n" +
                "2\n" +
                "0 0 0 0\n" +
                "1 0 11 11\n" +
                "11\n" +
                "0 1 1 0 1\n" +
                "0 2 1 3 5\n" +
                "0 6 1 4 5\n" +
                "0 8 1 5 5\n" +
                "0 10 1 5 6\n" +
                "1 3 1 11 9\n" +
                "1 4 1 5 8\n" +
                "1 5 1 5 7\n" +
                "1 7 1 6 6\n" +
                "1 9 1 6 5\n" +
                "1 11 1 6 4\n");

        Collection<GameCommand> commands = gameStrategy.buildCommands();
        List<String> strCommands = getStrCommands(commands);

        assertThat(strCommands).contains("TRAIN 1 5 4");
    }

    @Test
    public void test_GivenKillingOccasion_KillThemAll2() throws Exception {
        givenNewTurnInput("41 34 0 4\n" +
                "OOO#####.###\n" +
                "OOO##....###\n" +
                "OOOOOOOO####\n" +
                "###OOOO#####\n" +
                "##OOOOO##..#\n" +
                "#OOOOOOOx..#\n" +
                "#OOOOOOxx..#\n" +
                "#OO##OOxx.##\n" +
                "#####OOxx###\n" +
                "####OOOOOXoX\n" +
                "###OOOO##XXX\n" +
                "###.#####XXX\n" +
                "2\n" +
                "0 0 0 0\n" +
                "1 0 11 11\n" +
                "15\n" +
                "0 1 1 7 2\n" +
                "0 2 1 2 4\n" +
                "0 6 1 5 8\n" +
                "0 8 1 6 6\n" +
                "0 10 1 5 9\n" +
                "0 12 1 6 5\n" +
                "0 19 1 7 5\n" +
                "0 23 1 6 7\n" +
                "0 24 1 6 8\n" +
                "0 26 1 5 10\n" +
                "0 27 1 3 10\n" +
                "0 30 2 6 10\n" +
                "0 31 1 7 9\n" +
                "0 32 1 8 9\n" +
                "1 35 2 9 9");

        Collection<GameCommand> commands = gameStrategy.buildCommands();
        List<String> strCommands = getStrCommands(commands);

        assertThat(strCommands).contains("TRAIN 3 9 9");
    }

    @Test
    public void test_GivenDefensiveSituation_UnitShouldNotMove() throws Exception {
        givenNewTurnInput("15 8 6 26\n" +
                "OOOOOOO....#\n" +
                ".OOOOOO###.#\n" +
                "...#OOOO##o#\n" +
                "#.##OOOOO#o#\n" +
                "#.##OOOOOOX#\n" +
                "#..##xOOOOX#\n" +
                "#.ooxxO##OX#\n" +
                "#.oXOOOO##X#\n" +
                "#.#XXXXX##X#\n" +
                "#.##XXXX#.X.\n" +
                "#.###XXXXXXX\n" +
                "#....XXXXXXX\n" +
                "5 0 0 0 0 1 0 11 11 1 2 4 9 1 2 6 8 1 2 10 6 15 0 1 1 7 4 0 2 1 7 5 0 5 1 6 5 0 8 1 5 7 0 10 1 8 5 0 11 1 6 7 0 13 1 7 7 0 15 1 8 4 0 16 1 9 5 0 19 1 9 6 0 26 3 4 7 1 3 1 5 9 1 6 1 7 9 1 7 1 3 7 1 27 1 10 4");

        Collection<GameCommand> commands = gameStrategy.buildCommands();
        List<String> strCommands = getStrCommands(commands);

        assertThat(strCommands)
                .doesNotContain("MOVE 8 5 6")
                .doesNotContain("MOVE 8 4 7");
    }

    private void givenNewTurnInput(String input) {
        GameReader reader = new GameReader(new GameInputScanner(new Scanner(new StringReader(input))));
        reader.readNewTurn(game.onNewTurn());
    }

    private List<String> getStrCommands(Collection<GameCommand> commands) {
        return commands.stream()
                .map(GameCommand::getFormattedCommand)
                .collect(Collectors.toList());
    }

}