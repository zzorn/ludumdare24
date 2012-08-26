package org.ludumdare24;

import org.gameflow.services.sound.SoundResourceHandle;

/**
 * The available sound files.
 */
public enum Sounds implements SoundResourceHandle {
    UI_CLICK("sounds/click.wav"),
    UI_ACCEPT("sounds/bibump.wav"),
    QUIT("sounds/zing.wav"),
    JUMP("sounds/jump.wav"),
    SMITE1("sounds/smite.wav"),
    SMITE2("sounds/smite2.wav"),
    SMITE3("sounds/smite3.wav"),
    RAGE("sounds/rage.wav"),
    LOVE("sounds/love.wav"),
    KISS("sounds/kiss.wav")
    ;

    private final String fileName;

    private Sounds(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

