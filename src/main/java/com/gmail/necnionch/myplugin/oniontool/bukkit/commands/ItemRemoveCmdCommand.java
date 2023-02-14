package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import com.gmail.necnionch.myplugin.oniontool.bukkit.PlayerSession;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemRemoveCmdCommand implements OnionCommand {

    private final OnionToolPlugin plugin;

    public ItemRemoveCmdCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionremovecmditem";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "削除するコマンド番号を指定してください");
            return true;
        } else if (!args[0].equalsIgnoreCase("*") && !args[0].matches("\\d+")) {
            sender.sendMessage(ChatColor.RED + "数字で指定してください");
            return true;
        }

        Integer num = args[0].equalsIgnoreCase("*") ? null : Integer.parseInt(args[0]);
        Material itemType = sender.getInventory().getItemInMainHand().getType();
        if (Material.AIR.equals(itemType)) {
            sender.sendMessage(ChatColor.RED + "アイテムを手に持って実行してください");
            return true;
        }

        PlayerSession session = plugin.getSessionOrCreate(sender);
        List<String> list = session.getItemCommandList(itemType);
        if (list.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "アイテム " + itemType + " にコマンドが設定されていません");
            return true;
        }

        if (num != null) {
            String removed;
            try {
                removed = list.remove(num - 1);
            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(ChatColor.RED + "指定可能な番号は 1 から " + list.size() + " までです");
                return true;
            }
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "アイテム " + itemType + " からコマンド(#" + num + ")を削除しました: " + ChatColor.WHITE + removed);

        } else {
            list.clear();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "アイテム " + itemType + " からコマンドを全て削除しました");
        }

        if (list.isEmpty())
            session.clearItemCommandList(itemType);

        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.cmditem");
    }
}
