package com.github.bigibas123.bigidiscordbot.sound;

import com.github.bigibas123.bigidiscordbot.Main;
import com.github.bigibas123.bigidiscordbot.util.Emoji;
import com.github.bigibas123.bigidiscordbot.util.Utils;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.UserImpl;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class SearchResultHandler<T> extends ListenerAdapter {

    @Getter
    private final List<TrackInfo<T>> assignments;

    private final TextChannel channel;
    private final User author;
    private JDA jda;
    private Emoji[] oneToTen = new Emoji[] { Emoji.ONE, Emoji.TWO, Emoji.THREE, Emoji.FOUR, Emoji.FIVE, Emoji.SIX, Emoji.SEVEN, Emoji.EIGHT, Emoji.NINE, Emoji.TEN };
    private final MessageEmbed embed;
    private Message message;

    public SearchResultHandler(TextChannel channel, User author, ArrayList<TrackInfo<T>> playlist, JDA jda) {
        this.channel = channel;
        this.author = author;
        this.jda = jda;
        this.assignments = playlist.subList(0, Math.min(playlist.size(), 10));
        this.embed = buildEmbed();

    }

    private MessageEmbed buildEmbed() {
        EmbedBuilder ebb = new EmbedBuilder();
        ebb.setFooter("Requested by @" + author.getName(), author.getEffectiveAvatarUrl());
        ebb.setTitle("Search results");
        ebb.setColor(Color.MAGENTA);
        StringBuilder number = new StringBuilder();
        StringBuilder title = new StringBuilder();
        StringBuilder time = new StringBuilder();
        boolean first = true;
        for (TrackInfo<T> track: assignments) {
            if (first) {
                first = false;
            } else {
                number.append("\r\n");
                title.append("\r\n");
                time.append("\r\n");
            }
            String t = track.getTitle();
            number.append(track.getNumber());
            title.append(t, 0, Math.min(t.length(), 40));
            time.append(Utils.formatDuration(track.getDuration()));
        }

        ebb.addField("Number", number.toString(), true);
        ebb.addField("Title", title.toString(), true);
        ebb.addField("Time", time.toString(), true);
        return ebb.build();
    }

    @Override
    public void onMessageEmbed(@Nonnull MessageEmbedEvent event) {
        if (Utils.isSameThing(event.getChannel(), message.getChannel())) {
            if (!event.getMessageId().equals(message.getId())) {
                MessageHistory mh = event.getChannel().getHistoryAround(event.getMessageId(), 1).complete();
                Message newMessage = mh.getRetrievedHistory().get(0);
                if (Utils.isSameThing(newMessage.getAuthor(), jda.getSelfUser())) {
                    message.clearReactions().queue(s -> {
                    }, f -> {
                        if (f instanceof InsufficientPermissionException) {
                            Stream.of(oneToTen).forEach(a -> message.removeReaction(a.s()).queue());
                        }
                    });
                    this.jda.removeEventListener(this);
                }
            }
        }
    }



    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if ((!Utils.isSameThing(event.getUser(), jda.getSelfUser())) &&
                Utils.isDJ(event.getUser(), event.getGuild()) &&
                Utils.isSameThing(event.getChannel(), this.channel) &&
                event.getMessageId().equals(message.getId())) {

            String toCompare = event.getReactionEmote().getName();

            int nummer = -1;
            for (int i = 0; i < oneToTen.length; i++) {
                if (oneToTen[i].s().equals(toCompare)) {
                    nummer = i + 1;
                    break;
                }
            }
            if (nummer != -1) {
                int finalNummer = nummer;
                Optional<TrackInfo<T>> oTI = this.assignments.stream().filter(l -> l.getNumber() == finalNummer).findAny();
                if (oTI.isPresent()) {
                    TrackInfo<T> t = oTI.get();
                    String title = t.getTitle();
                    if (this.selected(t)) {
                        channel.sendMessage(this.author.getAsMention() + " track " + title + " queued").queue();
                    } else {
                        channel.sendMessage(this.author.getAsMention() + "track " + title + " not queued something went wrong").queue();
                    }
                } else {
                    message.addReaction(Emoji.WARNING.s()).queue();
                    Main.log.debug("Message in: " + event.getGuild().getName() + "->" + event.getChannel().getName() + "->" + event.getMessageId() + " got wrong emote " + Utils.getReactionEmoteLogString(event.getReactionEmote()));
                }
            } else {
                message.addReaction(Emoji.WARNING.s()).queue();
                Main.log.debug("Message in: " + event.getGuild().getName() + "->" + event.getChannel().getName() + "->" + event.getMessageId() + " got wrong emote " + Utils.getReactionEmoteLogString(event.getReactionEmote()));
            }
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                //if i do this it works even when not caching the user
                if (event.getReactionEmote().isEmote()) {
                    message.removeReaction(event.getReactionEmote().getEmote(), new UserImpl(event.getUserIdLong(), null)).queue();
                } else {
                    message.removeReaction(event.getReactionEmote().getEmoji(), new UserImpl(event.getUserIdLong(), null)).queue();
                }

            }
        }
    }

    protected abstract boolean selected(TrackInfo<T> track);

    public void go() {

        channel.sendMessage(embed).queue(result -> {
            this.message = result;
            this.jda.addEventListener(this);
            IntStream.range(0, Math.min(10, assignments.size())).forEach(i -> this.message.addReaction(oneToTen[i].s()).queue());
        });

    }
}
