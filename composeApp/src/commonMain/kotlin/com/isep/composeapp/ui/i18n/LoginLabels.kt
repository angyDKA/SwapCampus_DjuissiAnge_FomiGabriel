package com.isep.composeapp.ui.i18n

import androidx.compose.runtime.Composable
import com.isep.composeapp.resources.Res
import com.isep.composeapp.resources.login_error_empty_fields
import com.isep.composeapp.resources.login_error_invalid_credentials
import com.isep.composeapp.ui.viewmodel.LoginError
import org.jetbrains.compose.resources.stringResource

/**
 * Turns a [LoginError] into a text in the current language.
 * Common version: works on Android and on iOS.
 */
@Composable
fun LoginError.text(): String = stringResource(
    when (this) {
        LoginError.EMPTY_FIELDS -> Res.string.login_error_empty_fields
        LoginError.INVALID_CREDENTIALS -> Res.string.login_error_invalid_credentials
    }
)
