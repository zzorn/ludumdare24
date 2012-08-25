package org.gameflow.services.options;

import org.gameflow.Service;

/**
 * Service for managing players settings.
 */
public interface OptionsService extends Service {

    /**
     * Set an option.
     */
    void set(String key, Object value);

    /**
     * Gets an option, or the default value if the option was not found.
     */
    <T> T get(String key, T defaultValue);

    /**
     * @return true if there is an option with the specified key.
     */
    boolean has(String key);
}
