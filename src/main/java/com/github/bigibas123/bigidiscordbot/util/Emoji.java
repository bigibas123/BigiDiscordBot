package com.github.bigibas123.bigidiscordbot.util;
//https://itpro.cz/juniconv/
public enum Emoji {
    STOP_SIGN("\uD83D\uDED1"),  //🛑
    RUNNER("\uD83C\uDFC3"),     //🏃
    CHECK_MARK("\u2705"),       //✅
    CROSS("\u274C"),            //❌
    STOP_WATCH("\u23F1"),       //⏱
    SHRUG("\uD83E\uDD37"),      //🤷
    WAVE("\uD83D\uDC4B"),       //👋
    PAUSE("\u23F8"),            //⏸
    PLAY("\u25B6"),             //▶
    FAST_FORWARD("\u23E9"),     //⏩
    ONE("\u0031\u20E3"),        //1️⃣
    TWO("\u0032\u20E3"),        //2️⃣
    THREE("\u0033\u20E3"),      //3️⃣
    FOUR("\u0034\u20E3"),       //4️⃣
    FIVE("\u0035\u20E3"),       //5️⃣
    SIX("\u0036\u20E3"),        //6️⃣
    SEVEN("\u0037\u20E3"),      //7️⃣
    EIGHT("\u0038\u20E3"),      //8️⃣
    NINE("\u0039\u20E3"),       //9️⃣
    TEN("\uD83D\uDD1F"),        //🔟
    WARNING("\u26A0")           //⚠️
    ;

    private final String toString;

    Emoji(String s) {
        this.toString = s;
    }

    public String s() {
        return this.toString;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}
