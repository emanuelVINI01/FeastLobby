package com.emanuelvini.feastlobby.commands.registry;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.commands.GameModeCommand;
import com.emanuelvini.feastlobby.commands.LobbyAdminCommand;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import lombok.AllArgsConstructor;
import lombok.val;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;

@AllArgsConstructor
public class CommandRegistry {

    private FeastLobby plugin;

    public void register() {
        val frame = new BukkitFrame(plugin);

        frame.getMessageHolder().setMessage(MessageType.NO_PERMISSION, MessageValue.get(MessageValue::notHavePermission));
        frame.getMessageHolder().setMessage(MessageType.ERROR, MessageValue.get(MessageValue::errorOccurred));

        frame.registerCommands(new LobbyAdminCommand(), new GameModeCommand());
    }

}
