package org.maxgamer.quickshop.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.*;
import org.maxgamer.quickshop.Shop.Shop;

public class ShopSuccessPurchaseEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Shop shop;
    private Player p;
    private int amount;
    private boolean cancelled;
    private double tax;

    /**
     * Builds a new shop purchase event
     * This time, purchase not start, please listen the ShopSuccessPurchaseEvent.
     *
     * @param shop   The shop bought from
     * @param p      The player buying
     * @param amount The amount they're buying
     */
    public ShopSuccessPurchaseEvent(Shop shop, Player p, int amount, double tax) {
        this.shop = shop;
        this.p = p;
        this.amount = amount;
        this.tax = tax;
    }

    /**
     * The shop used in this event
     *
     * @return The shop used in this event
     */
    public Shop getShop() {
        return this.shop;
    }

    /**
     * The player trading with the shop
     *
     * @return The player trading with the shop
     */
    public Player getPlayer() {
        return this.p;
    }

    /**
     * The amount the purchase was for
     *
     * @return The amount the purchase was for
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Get the tax in this purchase
     *
     * @return Tax
     */
    public double getTax() {
        return tax;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}