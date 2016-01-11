package au.ryanlea.strings;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class MathsTest {

    @Test
    public void nextPowerOf2() {
        assertThat(Maths.nextPowerOf2(4), equalTo(8));
        assertThat(Maths.nextPowerOf2(127), equalTo(128));
        assertThat(Maths.nextPowerOf2(134978), equalTo(262144));
        assertThat(Maths.nextPowerOf2(23948), equalTo(32768));
    }

}