package com.maxxxwk.sportseventsapp.core.uitext

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UIText {
    data class StringResource(
        @StringRes val strRes: Int,
        val args: List<Any> = emptyList()
    ) : UIText

    data class DynamicString(val text: String) : UIText

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> text
        is StringResource -> stringResource(strRes, args)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> text
        is StringResource -> context.getString(strRes, args)
    }
}
