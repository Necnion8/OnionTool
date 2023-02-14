package com.gmail.necnionch.myplugin.oniontool.bukkit;

import com.gmail.necnionch.myplugin.oniontool.bukkit.commands.*;
import com.gmail.necnionch.myplugin.oniontool.bukkit.listeners.PlayerListener;
import com.google.common.collect.Maps;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class OnionToolPlugin extends JavaPlugin {
    private final Map<UUID, PlayerSession> sessions = Maps.newHashMap();
    private final List<OnionCommand> commands = Arrays.asList(
            new GUICommand(this),
            new HealCommand(this),
            new GodCommand(this),
            new UnGodCommand(this),
            new DamageViewCommand(this),
            new DamageReflectCommand(this),
            new RepairItemCommand(),
            new ItemAddCmdCommand(this),
            new ItemRemoveCmdCommand(this),
            new ItemListCmdCommand(this),
            new BrightCommand()
    );

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        commands.forEach(cmd -> Optional.ofNullable(getCommand(cmd.getName()))
                .ifPresent(c -> c.setExecutor(cmd)));
    }

    @Override
    public void onDisable() {
        sessions.values().forEach(PlayerSession::cancelTasks);
        sessions.clear();
    }


    public Optional<PlayerSession> getSession(Player player) {
        return Optional.ofNullable(sessions.get(player.getUniqueId()));
    }

    public PlayerSession getSessionOrCreate(Player player) {
        return sessions.computeIfAbsent(player.getUniqueId(), (p) -> new PlayerSession(this, player));
    }

    public void clearSession(UUID playerId) {
        PlayerSession session = this.sessions.remove(playerId);
        if (session != null)
            session.cancelTasks();
    }

    public Map<UUID, PlayerSession> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }

    public List<OnionCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }


    public double healFull(LivingEntity entity) {
        @SuppressWarnings("deprecation")
        double fullHealth = Optional.ofNullable(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                .map(AttributeInstance::getValue)
                .orElse(entity.getMaxHealth());
        double health = entity.getHealth();
        entity.setHealth(fullHealth);

        if (entity instanceof Player) {
            ((Player) entity).setFoodLevel(20);
        }
        return fullHealth - health;
    }


}
