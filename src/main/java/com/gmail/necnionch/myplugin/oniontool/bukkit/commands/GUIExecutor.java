package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface GUIExecutor {

    @NotNull String getLabel();

    void onSelect(@NotNull Player player);

    default @Nullable String getOneUsage(PlayerSession session) {
        if (this instanceof OnionCommand) {
            return Optional.ofNullable(JavaPlugin.getPlugin(OnionToolPlugin.class).getCommand(((OnionCommand) this).getName()))
                    .map(Command::getDescription)
                    .map(s -> ChatColor.stripColor(s.split("\\n", 2)[0]))
                    .orElse(null);
        }
        return null;
    }

    boolean hasPermission(Permissible permissible);

    default Boolean isActive(PlayerSession session) {
        return null;
    }

}
