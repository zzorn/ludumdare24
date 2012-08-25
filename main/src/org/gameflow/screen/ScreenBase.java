package org.gameflow.screen;

/**
 * Base class for game screens.
 */
public abstract class ScreenBase implements Screen {

    private final String id;

    private boolean created = false;
    private boolean disposed = false;

    /**
     * @param id an unique id for this screen.
     */
    protected ScreenBase(String id) {
        if (id != null) {
            this.id = id;
        }
        else {
            this.id = getClass().getSimpleName();
        }
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public final String getId() {
        return id;
    }



    public final void create() {
        if (!created) {
            created = true;
            doCreate();
        }
    }

    protected abstract void doCreate();

    public void resize(int width, int height) {}

    public void update(float deltaSeconds) {}

    public void render() {}

    public void show() {}

    public void hide() {}

    public void pause() {}

    public void resume() {}

    public final void dispose() {
        if (created && !disposed) {
            disposed = true;
            doDispose();
        }

    }

    protected abstract void doDispose();
}
