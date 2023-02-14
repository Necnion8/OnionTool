package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements OnionCommand {
    @Override
    public @NotNull String getName() {
        return "onionaddcmditem";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        return OnionCommand.super.onCommand(sender, args);
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.addcmditem");
    }
}
