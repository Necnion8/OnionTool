package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GodCommand implements OnionCommand, GUIExecutor {

    private final OnionToolPlugin plugin;

    public GodCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "oniongod";
    }

    @Override
    public @NotNull String getLabel() {
        return "God";
    }

    @Override
    public @Nullable String getOneUsage(PlayerSession session) {
        return "無敵モードを" + (session.isInvulnerable() ? "無効にします" : "有効にします");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                setGod(sender, plugin.getSessionOrCreate(((Player) sender)), true);
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
                setGod(sender, plugin.getSessionOrCreate(target), true);
            }
        }
        return true;
    }

    @Override
    public void onSelect(@NotNull Player player) {
        setGod(player, plugin.getSessionOrCreate(player), null);
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

    public static void setGod(CommandSender sender, PlayerSession session, Boolean active) {
        boolean god = Optional.ofNullable(active).orElse(!session.isInvulnerable());
        session.setInvulnerable(god);
        session.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE
                + "無敵モードが" + (session.isInvulnerable() ? "有効" : "無効") + "になりました！" + (session.getPlayer().equals(sender) ? "" : " by " + sender.getName()));
    }

    @Override
    public Boolean isActive(PlayerSession session) {
        return session.isInvulnerable();
    }
}
