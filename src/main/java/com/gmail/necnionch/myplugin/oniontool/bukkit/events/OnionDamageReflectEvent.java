package com.gmail.necnionch.myplugin.oniontool.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class OnionDamageReflectEvent extends EntityDamageByEntityEvent {
    public static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final EntityDamageByEntityEvent damageEvent;
    private boolean cancelled;

    public OnionDamageReflectEvent(Player player, EntityDamageByEntityEvent damageEvent) {
        super(damageEvent.getEntity(), damageEvent.getDamager(), damageEvent.getCause(), damageEvent.getDamage());
        this.player = player;
        this.damageEvent = damageEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityDamageByEntityEvent getOriginalDamageEvent() {
        return damageEvent;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
