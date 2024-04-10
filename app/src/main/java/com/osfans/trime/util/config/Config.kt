// SPDX-FileCopyrightText: 2015 - 2024 Rime community
//
// SPDX-License-Identifier: GPL-3.0-or-later

package com.osfans.trime.util.config

import com.osfans.trime.data.base.DataManager

/**
 * New YAML config parser intended to replace the old one.
 */
class Config(private val data: ConfigData = ConfigData()) {
    companion object {
        fun create(fileName: String): Config? {
            val data = ConfigData()
            return if (data.loadFromFile(DataManager.resolveDeployedResourcePath(fileName))) {
                Config(data)
            } else {
                null
            }
        }
    }

    fun loadFromFile(fileName: String) = data.loadFromFile(fileName)

    fun isNull(path: String): Boolean {
        val p = data.traverse(path)
        return p == null || p.type == ConfigItem.ValueType.Null
    }

    fun isValue(path: String): Boolean {
        val p = data.traverse(path)
        return p == null || p.type == ConfigItem.ValueType.Scalar
    }

    fun isList(path: String): Boolean {
        val p = data.traverse(path)
        return p == null || p.type == ConfigItem.ValueType.List
    }

    fun isMap(path: String): Boolean {
        val p = data.traverse(path)
        return p == null || p.type == ConfigItem.ValueType.Map
    }

    fun getBool(
        path: String,
        defValue: Boolean = false,
    ): Boolean {
        val p = data.traverse(path)?.configValue
        return runCatching { p?.getBool() }.getOrNull() ?: defValue
    }

    fun getInt(
        path: String,
        defValue: Int = 0,
    ): Int {
        val p = data.traverse(path)?.configValue
        return runCatching { p?.getInt() }.getOrNull() ?: defValue
    }

    fun getFloat(
        path: String,
        defValue: Float = 0f,
    ): Float {
        val p = data.traverse(path)?.configValue
        return runCatching { p?.getFloat() }.getOrNull() ?: defValue
    }

    fun getString(
        path: String,
        defValue: String = "",
    ): String {
        val p = data.traverse(path)?.configValue
        return runCatching { p?.getString() }.getOrNull() ?: defValue
    }

    fun getItem(path: String): ConfigItem? {
        return data.traverse(path)
    }

    fun getValue(path: String): ConfigValue? {
        return data.traverse(path)?.configValue
    }

    fun getList(path: String): ConfigList? {
        return data.traverse(path)?.configList
    }

    fun getMap(path: String): ConfigMap? {
        return data.traverse(path)?.configMap
    }

    fun getItem() = data.root
}
