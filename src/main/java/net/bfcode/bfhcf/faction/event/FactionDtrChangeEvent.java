package net.bfcode.bfhcf.faction.event;

import org.bukkit.event.HandlerList;

import net.bfcode.bfhcf.faction.struct.Raidable;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class FactionDtrChangeEvent extends Event implements Cancellable
{
    private static HandlerList handlers;
    private DtrUpdateCause cause;
    private Raidable raidable;
    private double originalDtr;
    private boolean cancelled;
    private double newDtr;
    
    public FactionDtrChangeEvent(DtrUpdateCause cause, Raidable raidable, double originalDtr, double newDtr) {
        this.cause = cause;
        this.raidable = raidable;
        this.originalDtr = originalDtr;
        this.newDtr = newDtr;
    }
    
    public static HandlerList getHandlerList() {
        return FactionDtrChangeEvent.handlers;
    }
    
    public DtrUpdateCause getCause() {
        return this.cause;
    }
    
    public Raidable getRaidable() {
        return this.raidable;
    }
    
    public double getOriginalDtr() {
        return this.originalDtr;
    }
    
    public double getNewDtr() {
        return this.newDtr;
    }
    
    public void setNewDtr(double newDtr) {
        this.newDtr = newDtr;
    }
    
    public boolean isCancelled() {
        return this.cancelled || Math.abs(this.newDtr - this.originalDtr) == 0.0;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return FactionDtrChangeEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
    
    public enum DtrUpdateCause
    {
        REGENERATION, 
        MEMBER_DEATH;
    }
}
