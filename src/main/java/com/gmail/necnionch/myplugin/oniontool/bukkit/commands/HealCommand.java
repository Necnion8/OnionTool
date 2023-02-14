package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HealCommand implements OnionCommand, GUIExecutor {

    private final OnionToolPlugin plugin;

    public HealCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionheal";
    }

    @Override
    public @NotNull String getLabel() {
        return "Heal";
    }

    @Override
    public @Nullable String getOneUsage(PlayerSession session) {
        return "HPを全回復します";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                executeHeal(sender, Collections.singletonList((Player) sender));
            }
        } else {
            Collection<LivingEntity> targets;
            if (args[0].equalsIgnoreCase("*")) {
                targets = Lists.newArrayList(Bukkit.getOnlinePlayers());
            } else {
                try {
                    targets = Bukkit.selectEntities(sender, String.join(" ", args))
                            .stream()
                            .filter(e -> e instanceof LivingEntity)
                            .map(e -> (LivingEntity) e)
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "セレクタ指定が無効です: " + e.getMessage());
                    return true;
                }
            }
            executeHeal(sender, targets);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return OnionCommand.generateSuggests(args[0], Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName));
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.heal");
    }

    @Override
    public void onSelect(@NotNull Player player) {
        executeHeal(player, Collections.singletonList(player));
    }

    public void executeHeal(CommandSender sender, Collection<LivingEntity> targets) {
        targets.forEach((e) -> heal(sender, e));
    }

    private void heal(CommandSender sender, LivingEntity entity) {
        long healed = Math.round(plugin.healFull(entity));
        entity.sendMessage(ChatColor.LIGHT_PURPLE + "HPが全回復しました (+" + healed + ")" + (entity.equals(sender) ? "" : " by " + sender.getName()));
    }


}
