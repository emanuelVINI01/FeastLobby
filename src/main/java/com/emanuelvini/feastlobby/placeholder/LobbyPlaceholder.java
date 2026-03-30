package com.emanuelvini.feastlobby.placeholder;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.model.Server;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LobbyPlaceholder extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "feastlobby";
    }

    @NotNull
    public String getAuthor() {
        return "emanuelvini";
    }

    @NotNull
    public String getVersion() {
        return "1.0-BETA";
    }

    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        try {
            Server server = FeastLobby.getInstance().getServerRepository().getServer(params);
            if (server != null) {
                if (server.isMaintenance())
                    return "§cManutenção";
                return PlaceholderAPI.setPlaceholders(null, String.format("%%bungee_%s%%", new Object[] { server.getBungee() }));
            }
            return "";
        } catch (Exception e) {
            return null;
        }
    }
}
