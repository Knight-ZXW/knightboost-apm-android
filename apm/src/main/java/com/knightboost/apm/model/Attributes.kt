package com.knightboost.apm.model

class Attributes() {

    private val values = mutableMapOf<String, String>()

    fun put(name: String, value: String) {
        values[name] = value
    }

    fun getAttribute(name: String): String? {
        if (values.containsKey(name)) {
            return values[name]
        }
        return null
    }

}