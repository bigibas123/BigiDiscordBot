package com.github.bigibas123.bigidiscordbot.commands.music;

import com.github.bigibas123.bigidiscordbot.commands.general.HelpCommand;
import com.github.bigibas123.bigidiscordbot.sound.IGuildMusicManager;
import com.github.bigibas123.bigidiscordbot.util.ReplyContext;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Arrays;

public class PlayCommand extends MusicCommand {
	public PlayCommand() {
		super("play", "plays some music in your voicechannel", "<url|ytsearch:|scsearch:> [search_terms]", "p");
	}

	@Override
	public boolean execute(ReplyContext replyContext, String... args) {
		if (replyContext.getChannel().getType() == ChannelType.TEXT) {
			Member member = replyContext.getMember();
            if (member != null) {
                GuildVoiceState vs = member.getVoiceState();
                if (vs != null) {
                    VoiceChannel vc = vs.getChannel();
                    if (vc != null) {
                        if (args.length > 2) {
                            IGuildMusicManager<?> gmm = this.getGuildManager(replyContext);
                            String search = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            if (gmm.connect(vc)) {
                                gmm.queue(search, replyContext);
                                return true;
                            }
                        } else {
                            HelpCommand.sendCommandDescription(replyContext, "empty", "empty", "play");
                        }
                    } else {
                        replyContext.reply("you need to join a voice channel for this command to work");
                    }
                } else {
                    replyContext.reply("Error retrieving voiceState from member");
                }
            } else {
                replyContext.reply("Error retrieving member from message");
            }
        } else {
			replyContext.reply("Wrong channel type");
		}
		return false;
	}
}
