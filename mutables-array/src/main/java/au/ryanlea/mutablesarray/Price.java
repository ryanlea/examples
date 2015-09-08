package au.ryanlea.mutablesarray;

public class Price implements Mutable<Price> {

    private double bid;

    private double offer;

    public double bid() {
        return bid;
    }

    public Price bid(double bid) {
        this.bid = bid;
        return this;
    }

    public double offer() {
        return offer;
    }

    public Price offer(double offer) {
        this.offer = offer;
        return this;
    }

    @Override
    public Price reset() {
        bid = Double.NaN;
        offer = Double.NaN;
        return this;
    }
}
