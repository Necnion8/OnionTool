package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemListCmdCommand implements OnionCommand {

    private final OnionToolPlugin plugin;

    public ItemListCmdCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionlistcmditem";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        Material itemType = sender.getInventory().getItemInMainHand().getType();
        if (Material.AIR.equals(itemType)) {
            sender.sendMessage(ChatColor.RED + "アイテムを手に持って実行してください");
            return true;
        }

        List<String> list = plugin.getSessionOrCreate(sender).itemCommands().get(itemType);
        if (list == null || list.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "アイテム " + itemType + " にコマンドが設定されていません");
            return true;
        }

        sender.sendMessage(ChatColor.LIGHT_PURPLE + "アイテム " + itemType + " に設定されている実行コマンド:");
        for (int i = 0; i < list.size(); i++) {
            String command = list.get(i);
            sender.sendMessage(ChatColor.GOLD + "  " + (i + 1) + ". " + ChatColor.WHITE + command);
        }
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.cmditem");
    }
}
