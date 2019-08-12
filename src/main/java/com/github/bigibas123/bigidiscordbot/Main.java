package com.github.bigibas123.bigidiscordbot;


import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class Main {

    public static Logger log;

    public static void main(String[] args) throws LoginException {
        log = Logger.getLogger(Main.class.getCanonicalName());
        String token = System.getenv("DISCORD_TOKEN");
        JDA jda = new JDABuilder(token)
                .addEventListener(new Listener())
                .setAudioEnabled(true)
                .setBulkDeleteSplittingEnabled(true)
                .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
