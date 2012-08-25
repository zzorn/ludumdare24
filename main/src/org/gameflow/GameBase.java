package org.gameflow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import org.gameflow.screen.Screen;

/**
 * Base class for games.
 * Implementations should implement the setup() method, and add any needed services and screens from it.
 */
public abstract class GameBase implements Game {

    public static final String LOG_TAG = "BaseGame";
    private Array<Service> services = new Array<Service>();
    private Screen currentScreen = null;
    private boolean running = false;

    @Override
    public String getApplicationName() {
        return getClass().getSimpleName();
    }

    public final <T extends Service> T addService(T service) {
        if (running) throw new IllegalStateException("Can not add a service when the application is running.");
        if (services.contains(service, true)) throw new IllegalArgumentException("The game already contains the service '"+service.getServiceName()+"'");

        services.add(service);
        return service;
    }

    public final void setScreen(Screen screen) {
        if (currentScreen != screen) {
            String oldScreenName = currentScreen == null ? "None" : currentScreen.getId();
            String newScreenName = screen == null ? "None" : screen.getId();
            Gdx.app.debug(LOG_TAG, "Changing from screen " + oldScreenName + " to " + newScreenName);

            hideScreen(currentScreen);

            currentScreen = screen;

            showScreen(currentScreen);
        }
    }

    public final boolean isRunning() {
        return running;
    }

    /**
     * Called when the application is starting up.
     * Can be used to initialize and register the services and screens.
     */
    protected abstract void setup();

    /**
     * Called when setup is done and the services and current screen have been created.
     */
    protected void onSetupDone() {}

    /**
     * Called before shutdown of services and screens starts.
     */
    protected void onShutdownStarted() {}

    /**
     * Called after shutdown of services and screens is done.
     */
    protected void onShutdownDone() {}


    public final void create() {
        Gdx.app.log(LOG_TAG, getApplicationName() + " starting up");


        setup();

        running = true;

        for (Service service : services) {
            Gdx.app.log(LOG_TAG, "Starting service " + service.getServiceName());
            service.create();
        }

        showScreen(currentScreen);

        onSetupDone();
    }

    public final void resize(int width, int height) {
        Gdx.app.debug(LOG_TAG, "Resizing screen size to (" + width + ", " + height + ")");

        // Notify services
        for (Service service : services) {
            service.resize(width, height);
        }

        // Notify current screen
        if (currentScreen != null) {
            currentScreen .resize(width, height);
        }
    }

    public final void render() {
        // Do logic update
        doUpdate();

        // Notify services
        for (Service service : services) {
            service.render();
        }

        // Draw current screen
        if (currentScreen != null) {
            currentScreen.render();
        }
    }

    public final void pause() {
        Gdx.app.log(LOG_TAG, "Pausing");

        // Notify services
        for (Service service : services) {
            service.pause();
        }

        // Notify current screen
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    public final void resume() {
        Gdx.app.log(LOG_TAG, "Resuming");

        // Notify services
        for (Service service : services) {
            service.resume();
        }

        // Notify current screen
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    public final void dispose() {
        Gdx.app.log(LOG_TAG, "Shutting down " + getApplicationName());

        onShutdownStarted();

        // Close current screen
        hideScreen(currentScreen);

        // Close services
        services.reverse(); // Close in reversed order
        for (Service service : services) {
            Gdx.app.log(LOG_TAG, "Shutting down service " + service.getServiceName());
            service.dispose();
        }

        running = false;

        onShutdownDone();

        Gdx.app.log(LOG_TAG, "Shutdown complete");
    }


    private void doUpdate() {
        // TODO: More complicated logic updates could be done here.
        // TODO: See http://gafferongames.com/game-physics/fix-your-timestep/

        float deltaSeconds = Gdx.graphics.getDeltaTime();

        // Notify services
        for (Service service : services) {
            service.update(deltaSeconds);
        }

        // Update current screen
        if (currentScreen != null) {
            currentScreen.update(deltaSeconds);
        }
    }

    private void showScreen(Screen screen) {
        if (screen != null && running) {
            Gdx.app.log(LOG_TAG, "Creating screen " + screen.getId());
            screen.create();
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            screen.show();
        }
    }

    private void hideScreen(Screen screen) {
        if (screen != null && running) {
            Gdx.app.log(LOG_TAG, "Disposing screen " + screen.getId());
            screen.hide();
            screen.pause();
            screen.dispose();
        }
    }

}
