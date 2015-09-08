package au.ryanlea.hamcrest;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PriceTest {

    @Test
    public void hasProperties() {
        final Price price = new Price()
                .symbol("AUDUSD")
                .bid(0.9865)
                .ask(0.9875);

        assertThat(price, allOf(
                notNullValue(),
                hasProperty("bid",    closeTo(0.9865, 0.00001)),
                hasProperty("ask",    closeTo(0.9875, 0.00001)),
                hasProperty("spread", closeTo(0.001, 0.0001)),
                hasProperty("symbol", equalTo("AUDUSD"))
        ));
    }

}