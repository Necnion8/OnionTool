package com.gmail.necnionch.myplugin.oniontool.bukkit;

import com.gmail.necnionch.myplugin.oniontool.bukkit.commands.GUIExecutor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerSession {

    private Player player;
    private final OnionToolPlugin plugin;
    private @Nullable GUIInstance guiInstance;
    //
    private boolean invulnerable;
    private boolean viewDamages;
    private boolean reflectDamages;
    //
    private final Map<Material, List<String>> itemCommands = Maps.newHashMap();

    public PlayerSession(OnionToolPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Optional<GUIInstance> getGUIInstance() {
        return Optional.ofNullable(guiInstance);
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isViewDamages() {
        return viewDamages;
    }

    public void setViewDamages(boolean view) {
        this.viewDamages = view;
    }

    public boolean isReflectDamages() {
        return reflectDamages;
    }

    public void setReflectDamages(boolean reflectDamages) {
        this.reflectDamages = reflectDamages;
    }

    public Map<Material, List<String>> itemCommands() {
        return itemCommands;
    }

    public Set<Material> getItemCommandTypes() {
        return Collections.unmodifiableSet(itemCommands.keySet());
    }

    public List<String> getItemCommandList(Material material) {
        return itemCommands.computeIfAbsent(material, m -> Lists.newArrayList());
    }

    public void clearItemCommandList(Material material) {
        itemCommands.remove(material);
    }

    public int executeItemCommand(Material material) {
        List<String> commands = itemCommands.get(material);
        if (commands == null || commands.isEmpty())
            return 0;
        commands.forEach(cmd -> plugin.getServer().dispatchCommand(player, cmd));
        return commands.size();
    }


    public void cancelTasks() {
        stopGUI();
    }

    public void startGUI() {
        if (guiInstance != null)
            return;
        guiInstance = new GUIInstance();
        guiInstance.showDisplay();
        guiInstance.runTaskTimer(plugin, 0, 5);

        if (player.getInventory().getHeldItemSlot() == 0) {
            player.getInventory().setHeldItemSlot(1);
        } else if (player.getInventory().getHeldItemSlot() == 8) {
            player.getInventory().setHeldItemSlot(7);
        }

        plugin.getServer().getPluginManager().registerEvents(guiInstance, plugin);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
    }

    public void stopGUI() {
        if (guiInstance == null)
            return;
        guiInstance.cancel();
        guiInstance.clearDisplay();
        HandlerList.unregisterAll(guiInstance);
        guiInstance = null;
    }


    public class GUIInstance extends BukkitRunnable implements Listener {
        private int slotIdx;
        private final List<GUIExecutor> guiExecutors = plugin.getCommands().stream()
                .filter(cmd -> cmd instanceof GUIExecutor)
                .filter(e -> e.hasPermission(player))
                .map(cmd -> (GUIExecutor) cmd)
                .collect(Collectors.toList());
        private final long instancedTime = System.currentTimeMillis();

        public GUIInstance() throws IllegalStateException {
            guiExecutors.add(new CloseExecutor());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(
                    "切替: ホイール操作").color(ChatColor.YELLOW)
                    .append(" | ").color(ChatColor.GRAY)
                    .append("決定: クリック   ").color(ChatColor.YELLOW).create());
        }

        public void nextGUIItem() {
            int size = guiExecutors.size();
            slotIdx = (slotIdx + 1) % size;
            showDisplay(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }

        public void backGUIItem() {
            int size = guiExecutors.size();
            slotIdx = (slotIdx - 1) % size;
            if (slotIdx < 0)
                slotIdx += size;
            showDisplay(false);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }

        public void nextGUIItem_() {
            int size = guiExecutors.size();
            if (size <= slotIdx + 1)
                return;
            slotIdx++;
            showDisplay(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }

        public void backGUIItem_() {
            if (slotIdx <= 0)
                return;
            slotIdx--;
            showDisplay(false);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }

        @Override
        public void run() {
            showDisplay();
        }

        public void showDisplay() {
            showDisplay(null);
        }

        public void showDisplay_(Boolean curNext) {
            boolean displayUsage = System.currentTimeMillis() - instancedTime > 3000;
            int size = guiExecutors.size();
            GUIExecutor selected = guiExecutors.get(slotIdx);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {

                int ii = ((slotIdx + i) - size / 2) % size;
                if (ii < 0)
                    ii += size;
                GUIExecutor executor = guiExecutors.get(ii);

                if (i != 0)
                    sb.append("  ");
                if (selected.equals(executor)) {
                    sb.append(curNext != null && !curNext ? ChatColor.YELLOW + " > " : "   ");
                    sb.append(ChatColor.GOLD).append(executor.getLabel());
                    sb.append(curNext != null && curNext ? ChatColor.YELLOW + " <  " : "   ");

                    if (displayUsage)
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(
                                Optional.ofNullable(executor.getOneUsage(PlayerSession.this)).orElse(""))
                                .color(ChatColor.AQUA).bold(true).create());

                } else {
                    sb.append(ChatColor.GRAY).append(executor.getLabel());
                }

            }

            player.sendTitle("", sb.toString(), 0, 20, 10);
        }

        public void showDisplay(Boolean curNext) {
            boolean displayUsage = System.currentTimeMillis() - instancedTime > 3000;
            int size = guiExecutors.size();
            GUIExecutor selected = guiExecutors.get(slotIdx);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                GUIExecutor executor = guiExecutors.get(i);

                if (i != 0)
                    sb.append(" ");

                if (selected.equals(executor)) {
//                    sb.append("  ");
                    sb.append(ChatColor.GOLD).append(ChatColor.UNDERLINE).append(executor.getLabel()).append(ChatColor.GRAY);
//                    sb.append("  ");

                    if (displayUsage)
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(
                                Optional.ofNullable(executor.getOneUsage(PlayerSession.this)).orElse(""))
                                .color(ChatColor.AQUA).bold(true).create());

                } else {
                    Boolean active = executor.isActive(PlayerSession.this);
                    sb.append(active != null && active ? ChatColor.DARK_PURPLE : ChatColor.GRAY);
                    sb.append(executor.getLabel());
                }

            }

            player.sendTitle("", sb.toString(), 0, 20, 10);
        }

        public void clearDisplay() {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
            player.sendTitle("", "", 0, 0, 0);
        }

        public void executeSelected() {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
            guiExecutors.get(slotIdx).onSelect(player);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onCursor(PlayerItemHeldEvent event) {
            if (!event.getPlayer().equals(player))
                return;

            event.setCancelled(true);

            int previousSlot = event.getPreviousSlot();
            int newSlot = event.getNewSlot();
            if (previousSlot == newSlot) {
                return;
            } else if (previousSlot < newSlot) {
                nextGUIItem();
            } else {
                backGUIItem();
            }
            player.getInventory().setHeldItemSlot(previousSlot);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onInteract(PlayerInteractEvent event) {
            if (!event.getPlayer().equals(player))
                return;

            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);

            if (!event.getPlayer().isSneaking())
                stopGUI();

            executeSelected();
            if (event.getPlayer().isSneaking() && this.equals(guiInstance))
                showDisplay();  // refresh
        }

    }

    private class CloseExecutor implements GUIExecutor {

        @Override
        public @NotNull String getLabel() {
            return "Close";
        }

        @Override
        public void onSelect(@NotNull Player player) {
            stopGUI();
        }

        @Override
        public @Nullable String getOneUsage(PlayerSession session) {
            return "クイック画面を終了します";
        }

        @Override
        public boolean hasPermission(Permissible permissible) {
            return true;
        }

    }

}
