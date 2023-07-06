package com.emanuelvini.feastlobby.configuration.registry;

import com.emanuelvini.feastlobby.FeastLobby;
import com.emanuelvini.feastlobby.configuration.*;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationRegistry {

    private final FeastLobby plugin = FeastLobby.getInstance();

    public void register() {


        BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);

        configurationInjector.saveDefaultConfiguration(
                plugin,
                "messages.yml",
                "configuration.yml",
                "selector.yml"
        );

        configurationInjector.injectConfiguration(
                MessageValue.instance(),
                ErrorFixerValue.instance(),
                FeatureValue.instance(),
                ChatValue.instance(),
                SelectorValue.instance()
        );
        System.out.println(configurationInjector.getConfigurationLoader().getConfigurations().keySet());
        SelectorValue.servers((ConfigurationSection) configurationInjector.getConfigurationLoader().getConfigurations().get("selector.yml").get("inventory.servers"));

    }

}
