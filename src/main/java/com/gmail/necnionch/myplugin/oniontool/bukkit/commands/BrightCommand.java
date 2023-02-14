package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrightCommand implements OnionCommand, GUIExecutor {
    @Override
    public @NotNull String getName() {
        return "onionbright";
    }

    @Override
    public @NotNull String getLabel() {
        return "Bright";
    }

    @Override
    public @Nullable String getOneUsage(PlayerSession session) {
        return "暗視エフェクトを" + (session.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION) ? "削除します" : "付与します");

    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        if (sender.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            sender.removePotionEffect(PotionEffectType.NIGHT_VISION);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "暗視エフェクトを削除しました");
        } else {
            sender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "暗視エフェクトを付与しました");
        }
        return true;
    }

    @Override
    public void onSelect(@NotNull Player player) {
        onCommand(player, new String[0]);
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.bright");
    }

    @Override
    public Boolean isActive(PlayerSession session) {
        return session.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
