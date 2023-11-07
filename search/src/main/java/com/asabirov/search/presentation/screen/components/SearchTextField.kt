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
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    text: State<String>,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    iconSearch: @Composable (() -> Unit),
    iconLocation: @Composable (() -> Unit),
    hideKeyboard: Boolean = false,
    onFocusChanged: (FocusState, String) -> Unit
) {
    val textValue = text.value
    val focusManager = LocalFocusManager.current
    Box{
        OutlinedTextField(
            value = textValue,
            onValueChange = onValueChange,
            singleLine = true,
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
                .onFocusChanged { onFocusChanged(it, textValue) },
            leadingIcon = iconSearch,
            trailingIcon = iconLocation,
            label = { Text("Your city") }
        )
    }
    if (hideKeyboard) {
        focusManager.clearFocus()
//        onFocusClear(textValue)
    }
}