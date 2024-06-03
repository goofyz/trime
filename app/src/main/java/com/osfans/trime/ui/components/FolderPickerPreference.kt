// SPDX-FileCopyrightText: 2015 - 2024 Rime community
//
// SPDX-License-Identifier: GPL-3.0-or-later

package com.osfans.trime.ui.components

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.activity.result.ActivityResultLauncher
import androidx.preference.Preference
import com.osfans.trime.R
import com.osfans.trime.ui.setup.SetupFragment

class FolderPickerPreference
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int = androidx.preference.R.attr.preferenceStyle,
    ) : Preference(context, attrs, defStyleAttr) {
        private var value = ""
        lateinit var documentTreeLauncher: ActivityResultLauncher<Intent?>

        var default = ""

        init {
            context.theme.obtainStyledAttributes(attrs, R.styleable.FolderPickerPreferenceAttrs, 0, 0).run {
                try {
                    if (getBoolean(R.styleable.FolderPickerPreferenceAttrs_useSimpleSummaryProvider, false)) {
                        summaryProvider = SummaryProvider<FolderPickerPreference> { getDisplayValue(it.value) }
                    }
                } finally {
                    recycle()
                }
            }
        }

        override fun persistString(value: String): Boolean {
            return super.persistString(value).also {
                if (it) this.value = value
            }
        }

        override fun setDefaultValue(defaultValue: Any?) {
            super.setDefaultValue(defaultValue)
            default = defaultValue as? String ?: ""
        }

        override fun onGetDefaultValue(
            a: TypedArray,
            index: Int,
        ): Any {
            return a.getString(index) ?: default
        }

        override fun onSetInitialValue(defaultValue: Any?) {
            value = getPersistedString(defaultValue as? String ?: default)
        }

        override fun onClick()  {
            documentTreeLauncher.launch(SetupFragment.getFolderIntent())
        }

        private fun setValue(value: String) {
            if (callChangeListener(value)) {
                persistString(value)
                notifyChanged()
            }
        }

        fun assignValue(value: String) {
            if (value.isNotBlank()) {
                setValue(value)
            }
        }

        private fun getDisplayValue(value: String): String {
            return value.split("%3A").last().replace("%2F", "/")
        }
    }
