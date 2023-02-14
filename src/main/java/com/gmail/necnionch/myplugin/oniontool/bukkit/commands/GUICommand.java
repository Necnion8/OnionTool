package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

public class GUICommand implements OnionCommand {

    private final OnionToolPlugin plugin;

    public GUICommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "oniontoolgui";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        plugin.getSessionOrCreate(sender).startGUI();
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.gui");
    }
}
