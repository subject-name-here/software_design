package ru.iisuslik.cli

class VarsContainer {
    private val variables = mutableMapOf<String, String>()

    fun add(name: String, value: String) {
        variables["$$name"] = value
    }

    fun get(name: String) = variables[name] ?: ""
}