package au.ryanlea.hamcrest;

public class Price {

    private double bid = Double.NaN;

    private double ask = Double.NaN;

    private double spread;

    private String symbol;

    public Price bid(double bid) {
        this.bid = bid;
        calcSpread();
        return this;
    }

    public double bid() {
        return bid;
    }

    public Price ask(double ask) {
        this.ask = ask;
        calcSpread();
        return this;
    }

    public double ask() {
        return ask;
    }

    public double spread() {
        return spread;
    }

    public Price symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public String symbol() {
        return symbol;
    }

    private void calcSpread() {
        if (!Double.isNaN(bid) && !Double.isNaN(ask)) {
            spread = Math.abs(bid - ask);
        }
    }
}
