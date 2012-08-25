package org.ludumdare24.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.ludumdare24.MainGame;

/**
 * Starts the game as a desktop application.
 */
public class DesktopStarter {
    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "GameFlow Example";
        cfg.useGL20 = false;
        cfg.width = 800;
        cfg.height = 480;

        new LwjglApplication(new MainGame(), cfg);
    }
}
