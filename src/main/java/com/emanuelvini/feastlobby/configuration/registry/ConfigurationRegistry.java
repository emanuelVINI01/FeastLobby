package com.emanuelvini.feastlobby.configuration.registry;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.ChatValue;
import com.emanuelvini.feastlobby.configuration.ErrorFixerValue;
import com.emanuelvini.feastlobby.configuration.FeatureValue;
import com.emanuelvini.feastlobby.configuration.MessageValue;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;

public class ConfigurationRegistry {

    private final FeastLobby plugin = FeastLobby.getInstance();

    public void register() {


        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
                plugin,
                "messages.yml",
                "configuration.yml"
        );

        configurationInjector.injectConfiguration(
                MessageValue.instance(),
                ErrorFixerValue.instance(),
                FeatureValue.instance(),
                ChatValue.instance()
        );

    }

}
