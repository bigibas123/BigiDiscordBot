package com.github.bigibas123.bigidiscordbot.commands.music;

import com.github.bigibas123.bigidiscordbot.Main;
import com.github.bigibas123.bigidiscordbot.commands.ICommand;
import com.github.bigibas123.bigidiscordbot.sound.IGuildMusicManager;
import com.github.bigibas123.bigidiscordbot.util.ReplyContext;
import com.github.bigibas123.bigidiscordbot.util.Utils;
import net.dv8tion.jda.api.entities.*;

public abstract class MusicCommand extends ICommand {

    public MusicCommand(String name, String description, String syntax, String... aliases) {
        super(name, description, syntax, aliases);
    }

    protected IGuildMusicManager<?> getGuildManager(ReplyContext replyContext) {
        return Main.soundManager.getGuildMusicManager(replyContext.getGuild());
    }

    protected boolean guildManagerExists(ReplyContext replyContext) {
        return Main.soundManager.guildMusicManagerExists(replyContext.getGuild());
    }

    protected void stopGuildManager(ReplyContext replyContext){
        Main.soundManager.removeGuildMusicManager(replyContext.getGuild());
    }

    @Override
    public boolean hasPermission(User user, Member member, MessageChannel channel) {
        if (channel.getType().isGuild()) {
            if (channel instanceof TextChannel) {
                return Utils.isDJ(user,((TextChannel) channel).getGuild());
            } else {
                return false;
            }
        } else {
            return channel.getType() == ChannelType.GROUP || channel.getType() == ChannelType.PRIVATE;
        }
    }
}
