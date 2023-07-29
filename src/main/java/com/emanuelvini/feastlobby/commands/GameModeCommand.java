package com.emanuelvini.feastlobby.commands;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCommand {

    @Command(
            name = "gamemode",
            aliases = {"gm", "gmm"},
            permission = "feastlobby.admin.gamemode"
    )

    public void onGameMode(Context<Player> context, GameMode gameMode) {

    }

}
