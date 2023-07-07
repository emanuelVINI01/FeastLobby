package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@ConfigFile("configuration.yml")
@ConfigSection("features.file")
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class FileValue implements ConfigurationInjectable {

    @Getter private static final FileValue instance = new FileValue();

    @ConfigField("enable") private boolean enable;

    @ConfigField("delay") private int delay;

    @ConfigField("enable chat message") private boolean enableChatMessage;

    @ConfigField("enable actionbar message") private boolean enableActionbarMessage;

    @ConfigField("close inventory on join leave") private boolean closeInventoryOnJoinLeave;


    public static <T> T get(Function<FileValue, T> function) {
        return function.apply(instance);
    }

}
