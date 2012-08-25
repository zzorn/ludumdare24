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

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
    }

    @Override
    public void update(float deltaSeconds) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
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
