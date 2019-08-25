package com.github.bigibas123.bigidiscordbot.commands.testing;

import com.github.bigibas123.bigidiscordbot.Main;
import com.github.bigibas123.bigidiscordbot.Reference;
import com.github.bigibas123.bigidiscordbot.commands.ICommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class LongRunningCommand extends ICommand {
    public LongRunningCommand() {
        super("Long", "**TESTCOMMAND** takes 4s", "", "waitCommand", "sleepTest");
    }

    @Override
    public boolean execute(Message message, String... args) {
        try {
            Main.log.info("Tick");
            Thread.sleep(4000);
            Main.log.info("Tock");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean hasPermission(User user, MessageChannel channel) {
        return user.getId().equals(Reference.ownerID);
    }
}
