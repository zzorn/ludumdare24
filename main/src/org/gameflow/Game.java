package org.gameflow;

import com.badlogic.gdx.ApplicationListener;
import org.gameflow.screen.Screen;

/**
 * Interface for applications.
 */
public interface Game extends ApplicationListener {

    /**
     * @return the name of the application, for logging etc.  Defaults to the simple name of the class.
     */
    String getApplicationName();

    /**
     * Adds a service to the application.  Should be called in setup.
     * @param service the service to add.
     * @return the added service.
     */
    <T extends Service> T addService(T service);

    /**
     * Changes the currently active screen.
     * @param screen the screen to change to.
     */
    void setScreen(Screen screen);

    /**
     * @return true if the game is running (has been created but not yet disposed).
     */
    boolean isRunning();
}
