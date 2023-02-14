package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface OnionCommand extends TabExecutor {

    @NotNull String getName();

    default boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        return false;
    }

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player)
            return onCommand(((Player) sender), args);
        return false;
    }

    @Override
    default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }


    static List<String> generateSuggests(String arg, String... list) {
        return generateSuggests(arg, Stream.of(list));
    }

    static List<String> generateSuggests(String arg, Collection<String> list) {
        return generateSuggests(arg, list.stream());
    }

    static List<String> generateSuggests(String arg, Stream<String> list) {
        String arg_ = arg.toLowerCase(Locale.ROOT);
        return list
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(arg_))
                .collect(Collectors.toList());
    }

    boolean hasPermission(Permissible permissible);

}
