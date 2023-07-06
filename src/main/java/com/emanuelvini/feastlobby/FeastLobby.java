package com.emanuelvini.feastlobby;

import com.emanuelvini.feastcore.bukkit.api.BukkitFeastPlugin;
import com.emanuelvini.feastlobby.commands.LobbyAdminCommand;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import com.emanuelvini.feastlobby.configuration.registry.ConfigurationRegistry;
import com.emanuelvini.feastlobby.listeners.ChatListener;
import com.emanuelvini.feastlobby.listeners.PlayerListener;
import com.emanuelvini.feastlobby.listeners.WorldListener;
import com.emanuelvini.feastlobby.placeholder.LobbyPlaceholder;
import com.emanuelvini.feastlobby.repository.ServerRepository;
import lombok.Getter;
import lombok.val;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;

public class FeastLobby extends BukkitFeastPlugin {

    @Getter
    private ServerRepository serverRepository;

    @Getter
    private static FeastLobby instance;

    @Override
    public void setupDependencies() {
        addDependency("SmartInvs", "https://github.com/MinusKube/SmartInvs/releases/download/v1.2.7/SmartInvs-1.2.7.jar");

    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        getCustomLogger().log("\n" +
                "§d___________                     __    .____         ___.  ___.           ____   ________ \n" +
                "§d\\_   _____/___ _____    _______/  |_  |    |    ____\\_ |__\\_ |__ ___.__. \\   \\ /   /_   |\n" +
                "§d |    __)/ __ \\\\__  \\  /  ___/\\   __\\ |    |   /  _ \\| __ \\| __ <   |  |  \\   Y   / |   |\n" +
                "§d |     \\\\  ___/ / __ \\_\\___ \\  |  |   |    |__(  <_> ) \\_\\ \\ \\_\\ \\___  |   \\     /  |   |\n" +
                "§d \\___  / \\___  >____  /____  > |__|   |_______ \\____/|___  /___  / ____|    \\___/   |___|\n" +
                "§d     \\/      \\/     \\/     \\/                 \\/         \\/    \\/\\/                      \n", "b");

        getCustomLogger().log("Comandos carregados com sucesso.", "a");

        //Injeção de configurações (Configuration-Injector)

        getCustomLogger().log("Injetando e atualizando configurações...", "e");

        val configurationRegistry = new ConfigurationRegistry();

        configurationRegistry.register();

        getCustomLogger().log("Configurações injetadas e atualizadas com sucesso.", "a");


        getCustomLogger().log("Carregando os servidores..", "e");

        serverRepository = new ServerRepository(this);

        getCustomLogger().log("Servidores carregados com sucesso.", "a");

        getCustomLogger().log("Carregando comandos...", "e");
        //Registra o BukkitFrame da API (CommandFramework)

        val frame = new BukkitFrame(this);

        frame.getMessageHolder().setMessage(MessageType.NO_PERMISSION, MessageValue.get(MessageValue::notHavePermission));
        frame.getMessageHolder().setMessage(MessageType.ERROR, MessageValue.get(MessageValue::errorOccurred));

        frame.registerCommands(new LobbyAdminCommand());

        getCustomLogger().log("Comandos carregados com sucesso", "a");

        getCustomLogger().log("Carregando eventos...", "e");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);

        getCustomLogger().log("Eventos carregados com sucesso.", "a");

        getCustomLogger().log("Registrando palceholders...", "e");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getCustomLogger().log("PlaceholderAPI não está instalado! Hook desativado.", "c");
        } else {
            new LobbyPlaceholder().register();

            getCustomLogger().log("Placeholders registrados com sucesso.", "a");
        }

    }


}
