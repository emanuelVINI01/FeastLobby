package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("messages.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class MessageValue implements ConfigurationInjectable {

    @Getter private static final MessageValue instance = new MessageValue();

    @ConfigField("not_have_permission") private String notHavePermission;

    @ConfigField("error_occurred") private String errorOccurred;

    @ConfigField("not_exists_server") private String notExistsServer;

    @ConfigField("not_have_permission_to_chat") private String notHavePermissionToChat;

    @ConfigField("server_is_in_maintenance") private String serverIsInMaintenance;

    @ConfigField("position_in_file") private String positionInFile;

    @ConfigField("joined_in_file") private String joinedInFile;

    @ConfigField("leave_file") private String leaveFile;

    public static <T> T get(Function<MessageValue, T> function) {
        return function.apply(instance);
    }

}
