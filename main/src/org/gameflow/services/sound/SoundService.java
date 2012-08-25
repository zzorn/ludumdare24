package org.gameflow.services.sound;

import org.gameflow.Service;

/**
 * Service for managing sounds and music.
 */
public interface SoundService extends Service {

    /**
     * Plays the specified sound.
     */
    void play(SoundResourceHandle sound);

    /**
     * Plays the sound in the specified file.
     */
    void play(String soundFileName);

    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    void setVolume(float volume);

    /**
     * Enables or disabled the sound.
     */
    void setEnabled(boolean enabled);
}
