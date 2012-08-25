package org.gameflow.screen;

import com.badlogic.gdx.ApplicationListener;

/**
 * Interface for some screen in the application, e.g. main menu, game screen, options, etc.
 */
public interface Screen extends ApplicationListener  {

    boolean isCreated();

    /**
     * @return name of this screen.  Used e.g. when switching screens.
     */
    String getId();

    /**
     * Called when the screen has been activated.
     */
    void show();

    /**
     * Called when the screen has been closed.
     */
    void hide();

    /**
     * Logic update.
     * @param deltaSeconds number of seconds since last call.
     */
    void update(float deltaSeconds);
}
