package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("selector.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectorValue implements ConfigurationInjectable {

    @Getter private static final SelectorValue instance = new SelectorValue();

    //Item do seletor

    @ConfigField("item.name") private String itemName;

    @ConfigField("item.lore") private List<String> itemLore;

    @ConfigField("item.enable custom skull") private boolean enabledItemCustomSkull;

    @ConfigField("item.skull url") private String itemSkullUrl;

    @ConfigField("item.material") private String material;

    @ConfigField("item.slot") private int itemSlot;


    //Inventário do seletor

    @ConfigField("inventory.name") private String inventoryName;

    @ConfigField("inventory.size") private int inventorySize;

    @Getter
    @Setter
    private static ConfigurationSection servers;

    public static <T> T get(Function<SelectorValue, T> function) {
        return function.apply(instance);
    }

}
