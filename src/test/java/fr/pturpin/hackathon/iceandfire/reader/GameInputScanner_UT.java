package fr.pturpin.hackathon.iceandfire.reader;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameInputScanner_UT {

    @Test
    public void nextInt_GivenInputDelimitedBySpaceAndNewLine_YieldsTheIntegers() throws Exception {
        GameInputScanner source = givenInput("1 2\n3 4");

        assertThat(source.nextInt()).isEqualTo(1);
        assertThat(source.nextInt()).isEqualTo(2);
        assertThat(source.nextInt()).isEqualTo(3);
        assertThat(source.nextInt()).isEqualTo(4);
    }

    @Test
    public void nextLine_GivenMultipleLines_YieldsThem() throws Exception {
        GameInputScanner source = givenInput("first line\nsecond line");

        assertThat(source.nextLine()).isEqualTo("first line");
        assertThat(source.nextLine()).isEqualTo("second line");
    }

    private GameInputScanner givenInput(String input) {
        return GameInputScanner.fromString(input);
    }

}