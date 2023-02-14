package com.gmail.necnionch.myplugin.oniontool.bukkit.listeners;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import com.gmail.necnionch.myplugin.oniontool.bukkit.events.OnionDamageReflectEvent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class PlayerListener implements Listener {

    private final OnionToolPlugin plugin;

    public PlayerListener(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getSession(event.getPlayer()).ifPresent(session ->
                session.setPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getSession(event.getPlayer()).ifPresent(PlayerSession::cancelTasks);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onCancelDamage(EntityDamageEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        if (session == null)
            return;

        if (session.isInvulnerable()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamageView(EntityDamageEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        if (session == null)
            return;

        if (session.isViewDamages()) {
            double val = Math.round(event.getFinalDamage() * 10d) / 10d;

            String attack = event.getCause().name();
            if (event instanceof EntityDamageByEntityEvent) {
                String damager = ((EntityDamageByEntityEvent) event).getDamager().getName();
                attack += " (" + damager + ChatColor.AQUA + ")";

            } else if (event instanceof EntityDamageByBlockEvent) {
                Block block = ((EntityDamageByBlockEvent) event).getDamager();
                String damager = block == null ? "null" : String.format("%s,%d,%d,%d", block.getType(), block.getX(), block.getY(), block.getZ());
                attack += " (" + damager + ChatColor.AQUA + ")";
            }

            session.getPlayer().sendMessage(ChatColor.DARK_RED + "(被ダメ)  " + ChatColor.RED + val + ChatColor.DARK_AQUA + "  by " + ChatColor.AQUA + attack);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamageReflect(EntityDamageByEntityEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        Entity damager = event.getDamager();
        if (session == null || !session.isReflectDamages())
            return;

        Player player = session.getPlayer();
        if (damager instanceof Player) {
            PlayerSession damagerSession = plugin.getSessions().get(damager.getUniqueId());
            if (damagerSession.isReflectDamages()) {
                player.sendMessage(ChatColor.DARK_RED + "ダメージを反射できなかった･･･！");
                return;
            }
        } else if (!(damager instanceof LivingEntity)) {
            if (damager instanceof Projectile) {
                damager.setVelocity(player.getEyeLocation().subtract(damager.getLocation())
                        .toVector()
                        .multiply(10));
                damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_BLAZE_HURT, SoundCategory.PLAYERS, .5f, 2);
                event.setCancelled(true);
            }
            return;
        }

        OnionDamageReflectEvent newEvent = new OnionDamageReflectEvent(player, event);
        plugin.getServer().getPluginManager().callEvent(newEvent);

        if (newEvent.isCancelled())
            return;

        event.setCancelled(true);
        LivingEntity e = (LivingEntity) damager;
        e.damage(newEvent.getFinalDamage(), newEvent.getPlayer());

        if (!session.isViewDamages()) {
            double val = Math.round(newEvent.getFinalDamage() * 10d) / 10d;
            String attack = event.getCause().name() + " (" + e.getName() + ChatColor.AQUA + ")";
            player.sendMessage(ChatColor.DARK_RED + "(ダメージ反射)  " + ChatColor.RED + val + ChatColor.DARK_AQUA + "  to " + ChatColor.AQUA + attack);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPotionReflect(EntityPotionEffectEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        if (session == null || !session.isReflectDamages())
            return;

        PotionEffectType potionType = Optional.ofNullable(event.getNewEffect())
                .map(PotionEffect::getType)
                .orElse(null);

        if (potionType == null)
            return;

        if (PotionEffectType.WITHER.equals(potionType)
                || PotionEffectType.POISON.equals(potionType)
                || PotionEffectType.INCREASE_DAMAGE.equals(potionType)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFood(FoodLevelChangeEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        if (session == null)
            return;

        if (session.isInvulnerable()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        PlayerSession session = plugin.getSessions().get(event.getEntity().getUniqueId());
        if (session == null)
            return;

        session.stopGUI();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandItemInteract(PlayerInteractEvent event) {
        if (!EquipmentSlot.HAND.equals(event.getHand()))
            return;

        PlayerSession session = plugin.getSession(event.getPlayer()).orElse(null);
        if (session == null)
            return;

        if (0 < session.executeItemCommand(event.getMaterial())) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }


}
