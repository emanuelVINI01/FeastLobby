package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import java.util.function.Function;

@TranslateColors
@ConfigFile("configuration.yml")
@ConfigSection("features.chat")
public class ChatValue implements ConfigurationInjectable {
    public static ChatValue instance() {
        return instance;
    }

    private static final ChatValue instance = new ChatValue();

    @ConfigField("enabled")
    private boolean enabled;

    @ConfigField("format")
    private String format;

    public boolean enabled() {
        return this.enabled;
    }

    public String format() {
        return this.format;
    }

    public static <T> T get(Function<ChatValue, T> function) {
        return function.apply(instance);
    }
}
