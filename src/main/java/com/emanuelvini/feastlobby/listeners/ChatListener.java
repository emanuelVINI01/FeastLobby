package com.emanuelvini.feastlobby.listeners;

import com.emanuelvini.feastlobby.configuration.ChatValue;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("feastlobby.chat")) {
            event.getPlayer().sendMessage((String)MessageValue.get(MessageValue::notHavePermissionToChat));
            event.setCancelled(true);
            return;
        }
        String message = event.getMessage().replace("%", "%%");
        String format = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(event.getPlayer(), (String)ChatValue.get(ChatValue::format)));
        format = format.replace("%", "%%");
        format = format.replace("%%name%%", event.getPlayer().getDisplayName());
        format = format.replace("{message}", message);
        event.setFormat(format);
    }
}