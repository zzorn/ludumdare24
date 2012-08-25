package org.gameflow.services.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import org.gameflow.ServiceBase;
import org.gameflow.utils.LRUCache;

/**
 * Based on the SoundManager in http://steigert.blogspot.fi/2012/03/8-libgdx-tutorial-sound-and-music.html
 */
public class SoundServiceImpl extends ServiceBase implements SoundService {

    private static final int MAX_SOUNDS = 20;

    /**
     * The volume to be set on the sound.
     */
    private float volume = 1f;

    /**
     * Whether the sound is enabled.
     */
    private boolean enabled = true;

    /**
     * The sound cache.
     */
    private final LRUCache<String, Sound> soundCache = new LRUCache<String, Sound>(MAX_SOUNDS);

    @Override
    public void create() {
        soundCache.setEntryRemovedListener(new LRUCache.CacheEntryRemovedListener<String, Sound>() {
            @Override
            public void notifyEntryRemoved(String key, Sound value) {
                logDebug("Disposing sound: " + key);
                value.dispose();
            }
        });
    }

    @Override
    public void play(SoundResourceHandle sound) {
        play(sound.getFileName());
    }

    @Override
    public void play(String soundFileName)
    {
        // Check if the sound is enabled
        if( ! enabled ) return;

        // Try and get the sound from the cache
        Sound soundToPlay = soundCache.get(soundFileName);
        if (soundToPlay == null) {
            FileHandle soundFile = Gdx.files.internal(soundFileName);
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add(soundFileName, soundToPlay);
        }

        // Play the sound
        logDebug("Playing sound: " + soundFileName);
        soundToPlay.play( volume );
    }

    @Override
    public void setVolume(float volume) {
        logDebug("Adjusting sound volume to: " + volume);

        // Check and set the new volume
        if( volume < 0 || volume > 1f ) {
            throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
        }
        this.volume = volume;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void dispose() {
        for( Sound sound : soundCache.retrieveAll() ) {
            sound.stop();
            sound.dispose();
        }
    }




}
