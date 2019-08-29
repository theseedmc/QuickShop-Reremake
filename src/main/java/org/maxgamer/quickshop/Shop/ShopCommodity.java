package org.maxgamer.quickshop.Shop;

public interface ShopCommodity {
    /**
     * Get how many commondity in stack
     * @return The commondity in stack.
     */
    int getCommodityInStack();

    /**
     * Calling when buying
     * @return Success
     */
    boolean onBuying();

    /**
     * Calling when selling
     * @return Success
     */
    boolean onSelling();

}
