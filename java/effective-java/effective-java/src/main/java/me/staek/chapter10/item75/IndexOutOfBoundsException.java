package me.staek.chapter10.item75;

public class IndexOutOfBoundsException extends RuntimeException {
    private final int lowerBound;
    private final int upperBound;
    private final int index;

    /**
     * Constructs an IndexOutOfBoundsException.
     *
     * @param lowerBound the lowest legal index value
     * @param upperBound the highest legal index value plus one
     * @param index      the actual index value
     */
    public IndexOutOfBoundsException(int lowerBound, int upperBound,
                                     int index) {
        super(String.format(
                "Lower bound: %d, Upper bound: %d, Index: %d",
                lowerBound, upperBound, index));
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.index = index;
    }
}
