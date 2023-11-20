package com.asabirov.search.presentation.search.screen.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asabirov.search.R

@Composable
fun SearchScreenScaffold(content: @Composable (contentPadding: PaddingValues) -> Unit) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp),
                    action = {
                        TextButton(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                i.data = uri
                                context.startActivity(i)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) { Text(stringResource(id = R.string.go_to_settings)) }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) {
        content(it)
    }
}