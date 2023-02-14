package com.gmail.necnionch.myplugin.oniontool.bukkit.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

public class RepairItemCommand implements OnionCommand {
    @Override
    public @NotNull String getName() {
        return "onionrepairitem";
    }

    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull String[] args) {
        PlayerInventory inv = sender.getInventory();
        ItemStack itemStack = inv.getItemInMainHand();

        if (!(itemStack.getItemMeta() instanceof Damageable)) {
            sender.sendMessage(ChatColor.RED + "耐久値がないアイテムです");
            return true;
        }

        Damageable damageable = (Damageable) itemStack.getItemMeta();
        int old = damageable.getDamage();
        damageable.setDamage(0);
        itemStack.setItemMeta((ItemMeta) damageable);

        sender.sendMessage(ChatColor.LIGHT_PURPLE + "アイテムの耐久値を全回復しました (+" + old + ")");
        return true;
    }

    @Override
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission("oniontool.command.repairitem");
    }

}
