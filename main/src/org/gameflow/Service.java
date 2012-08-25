package org.gameflow;

import com.badlogic.gdx.ApplicationListener;

/**
 * Something that handles a sub-area of responsibility.
 */
public interface Service extends ApplicationListener {

    /**
     * @return name of the service.  For logging etc.
     */
    String getServiceName();

    /**
     * Logic update.
     * @param deltaSeconds number of seconds since last call.
     */
    void update(float deltaSeconds);

}