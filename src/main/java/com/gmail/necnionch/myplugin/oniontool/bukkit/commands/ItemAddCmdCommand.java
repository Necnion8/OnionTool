package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import com.gmail.necnionch.myplugin.oniontool.bukkit.OnionToolPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemAddCmdCommand implements OnionCommand {

    private final OnionToolPlugin plugin;

    public ItemAddCmdCommand(OnionToolPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "onionaddcmditem";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "実行するコマンドが引数に指定されていません");
            return true;
        }
        Material itemType = sender.getInventory().getItemInMainHand().getType();
        if (Material.AIR.equals(itemType)) {
            sender.sendMessage(ChatColor.RED + "アイテムを手に持って実行してください");
            return true;
        }

        List<String> list = plugin.getSessionOrCreate(sender).getItemCommandList(itemType);
        String command = String.join(" ", args);
        list.add(command);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "アイテム " + itemType + " にコマンド(#" + list.size()+ ")を追加しました: \n" + ChatColor.WHITE + command);
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.cmditem");
    }
}
