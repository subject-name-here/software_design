package ru.iisuslik.cli

/**
 * This class stores variables into map
 */
class VarsContainer {
    private val variables = mutableMapOf<String, String>()

    /**
     * Adds new var to map or update it
     */
    fun add(name: String, value: String) {
        variables[name] = value
    }

    /**
     * Returns var value or empty string if it doesn't exist
     */
    fun get(name: String) = variables[name] ?: ""

    /**
     * Copies variables from this container to given container.
     */
    fun copyTo(varsContainer: VarsContainer) {
        for (pair in variables) {
            varsContainer.add(pair.key, pair.value)
        }
    }
}