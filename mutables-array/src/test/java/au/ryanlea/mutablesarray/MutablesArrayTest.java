package au.ryanlea.mutablesarray;

import org.junit.Test;

public class MutablesArrayTest {

    private final MutablesArray<Price> prices = new MutablesArray<>(Price.class, 16);

    @Test
    public void sample() {
        double bid = 0, offer = 0;
        prices.clear();
        prices.create().bid(0.9837).offer(0.9845);
        prices.create().bid(0.9838).offer(0.9844);
        for (int i = 0; i < prices.size(); i++) {
            bid += prices.get(i).bid();
            offer += prices.get(i).offer();
        }
        System.out.printf("avg; bid: %.5f, offer: %.5f", bid / prices.size(), offer / prices.size());
    }

}
