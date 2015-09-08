package au.ryanlea.mutablesarray;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {

    private final List<Price> prices = new ArrayList<>();

    @Test
    public void sample() {
        double bid = 0, offer = 0;
        prices.clear(); // elements set to null => likely garbage
        prices.add(new Price().bid(0.9837).offer(0.9845)); // price needs to be created somewhere, maybe not here
        prices.add(new Price().bid(0.9838).offer(0.9844));
        for (int i = 0; i < prices.size(); i++) {
            bid += prices.get(i).bid();
            offer += prices.get(i).offer();
        }
        System.out.printf("avg; bid: %.5f, offer: %.5f", bid / prices.size(), offer / prices.size());
    }

}
