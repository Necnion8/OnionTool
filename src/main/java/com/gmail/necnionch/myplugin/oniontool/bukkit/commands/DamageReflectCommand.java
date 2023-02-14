package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageReflectCommand implements OnionCommand, GUIExecutor {

    private final OnionToolPlugin plugin;

    public DamageReflectCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionreflectdamage";
    }

    @Override
    public @NotNull String getLabel() {
        return "R.Dmg";
    }

    @Override
    public @Nullable String getOneUsage(PlayerSession session) {
        return "ダメージ反射機能を" + (session.isReflectDamages() ? "無効にします" : "有効にします");
    }

    @Override
    public void onSelect(@NotNull Player player) {
        onCommand(player, new String[0]);
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        PlayerSession session = plugin.getSessionOrCreate(sender);
        session.setReflectDamages(!session.isReflectDamages());
        session.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE
                + "ダメージ反射機能が" + (session.isReflectDamages() ? "有効" : "無効") + "になりました！");
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.reflectdamage");
    }

    @Override
    public Boolean isActive(PlayerSession session) {
        return session.isReflectDamages();
    }
}
