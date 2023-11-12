package com.asabirov.search.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
    iconLeft: @Composable (() -> Unit) = {},
    iconRight: @Composable (() -> Unit) = {},
    hideKeyboard: Boolean = false,
    label: String = "",
    onFocusChanged: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Box {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = false,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    defaultKeyboardAction(ImeAction.Search)
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            modifier = modifier
                .clip(RoundedCornerShape(5.dp))
                .padding(2.dp)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(text) },
            leadingIcon = iconLeft,
            trailingIcon = iconRight,
            label = { Text(label) }
        )
    }
    if (hideKeyboard) {
        focusManager.clearFocus()
    }
}