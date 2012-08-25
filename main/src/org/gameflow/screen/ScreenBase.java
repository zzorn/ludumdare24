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

    @Override
    public final String getId() {
        return id;
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void update(float deltaSeconds) {}

    @Override
    public void render() {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

}
