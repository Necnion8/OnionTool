package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.necnionch.myplugin.oniontool.bukkit.commands.GodCommand.setGod;

public class UnGodCommand implements OnionCommand {

    private final OnionToolPlugin plugin;

    public UnGodCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionungod";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                setGod(sender, plugin.getSessionOrCreate(((Player) sender)), false);
            }
        } else {
            Collection<Player> targets;
            if (args[0].equalsIgnoreCase("*")) {
                targets = Lists.newArrayList(Bukkit.getOnlinePlayers());
            } else {
                try {
                    targets = Bukkit.selectEntities(sender, String.join(" ", args))
                            .stream()
                            .filter(e -> e instanceof Player)
                            .map(e -> (Player) e)
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "セレクタ指定が無効です: " + e.getMessage());
                    return true;
                }
            }
            for (Player target : targets) {
                setGod(sender, plugin.getSessionOrCreate(target), false);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return OnionCommand.generateSuggests(args[0], Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName));
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.god");
    }

}
