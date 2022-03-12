package at.helpch.chatchat.command;

import at.helpch.chatchat.ChatChatPlugin;
import at.helpch.chatchat.util.ChannelUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Default;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class SwitchChannelCommand extends BaseCommand {

    private final ChatChatPlugin plugin;
    private final String command;

    public SwitchChannelCommand(@NotNull final ChatChatPlugin plugin, @NotNull final String command) {
        super(command);
        this.plugin = plugin;
        this.command = command;
    }

    @Default
    public void switchChannel(final Player player) {
        final var channels = plugin.configManager().channels().channels();
        final var channel = channels.entrySet()
                .stream()
                .filter(entry -> entry.getValue().commandName().equals(command))
                .findAny()
                .map(Map.Entry::getValue)
                .get(); // this should probably only ever throw if the person has changed command names without restarting

        final var audiencePlayer = plugin.audiences().player(player);

        if (!player.hasPermission(ChannelUtils.BASE_CHANNEL_PERMISSION + command)) {
            audiencePlayer.sendMessage(Component.text("You don't have permission to talk in this channel!", NamedTextColor.RED));
            return;
        }

        final var user = plugin.usersHolder().getUser(player.getUniqueId());
        user.channel(channel);
        audiencePlayer.sendMessage(Component.text("You have switched to the " + command + " channel!", NamedTextColor.GREEN));
    }
}
