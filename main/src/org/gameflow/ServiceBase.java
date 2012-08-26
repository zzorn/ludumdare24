package org.gameflow;

import com.badlogic.gdx.Gdx;

/**
 *
 */
public abstract class ServiceBase implements Service {

    @Override
    public String getServiceName() {
        return getClass().getSimpleName();
    }

    public void create() {
    }

    public void resize(int width, int height) {
    }

    public void render() {
    }

    public void update(float deltaSeconds) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }

    protected final void logDebug(String message) {
        Gdx.app.debug(getServiceName(), message);
    }
    protected final void logDebug(String message, Throwable exception) {
        Gdx.app.debug(getServiceName(), message, exception);
    }
    protected final void log(String message) {
        Gdx.app.log(getServiceName(), message);
    }
    protected final void log(String message, Exception exception) {
        Gdx.app.log(getServiceName(), message, exception);
    }
    protected final void logError(String message) {
        Gdx.app.error(getServiceName(), message);
    }
    protected final void logError(String message, Throwable exception) {
        Gdx.app.error(getServiceName(), message, exception);
    }
}
