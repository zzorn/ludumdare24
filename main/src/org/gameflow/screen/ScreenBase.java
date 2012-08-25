package org.gameflow.screen;

/**
 * Base class for game screens.
 */
public abstract class ScreenBase implements Screen {

    private final String id;

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

    public final String getId() {
        return id;
    }


    public void resize(int width, int height) {}

    public void update(float deltaSeconds) {}

    public void render() {}

    public void show() {}

    public void hide() {}

    public void pause() {}

    public void resume() {}

}
