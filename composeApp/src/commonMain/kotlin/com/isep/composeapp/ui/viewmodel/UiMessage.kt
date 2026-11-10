package com.isep.composeapp.ui.viewmodel

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * A message produced by a ViewModel and displayed by a screen.
 * The ViewModel cannot call stringResource() (only composables can),
 * so it returns either a translation key or a ready-made text.
 */
sealed interface UiMessage {
    data class Resource(val id: StringResource) : UiMessage


    data class Raw(val value: String) : UiMessage
}


@Composable
fun UiMessage.text(): String = when (this) {
    is UiMessage.Resource -> stringResource(id)
    is UiMessage.Raw -> value
}