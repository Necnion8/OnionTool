package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DamageViewCommand implements OnionCommand, GUIExecutor {

    private final OnionToolPlugin plugin;

    public DamageViewCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionviewdamage";
    }

    @Override
    public @NotNull String getLabel() {
        return "Damage";
    }

    @Override
    public @Nullable String getOneUsage(PlayerSession session) {
        return "被ダメージ表示機能を" + (session.isViewDamages() ? "無効にします" : "有効にします");
    }

    @Override
    public void onSelect(@NotNull Player player) {
        onCommand(player, new String[0]);
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        PlayerSession session = plugin.getSessionOrCreate(sender);
        session.setViewDamages(!session.isViewDamages());
        session.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE
                + "被ダメージ表示機能が" + (session.isViewDamages() ? "有効" : "無効") + "になりました！");
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.viewdamage");
    }

    @Override
    public Boolean isActive(PlayerSession session) {
        return session.isViewDamages();
    }
}
