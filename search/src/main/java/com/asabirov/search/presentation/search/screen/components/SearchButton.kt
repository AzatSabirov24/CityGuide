package com.asabirov.search.presentation.search.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.asabirov.search.R
import com.asabirov.search.domain.model.places.PlaceModel
import com.asabirov.search.presentation.event.SearchEvent
import com.asabirov.search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchButton(
    places: LazyPagingItems<PlaceModel>?,
    viewModel: SearchViewModel = hiltViewModel(),
    scope: CoroutineScope,
    keyboardHide: () -> Unit,
    lazyListState: LazyListState
) {
    Button(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        onClick = {
            viewModel.onEvent(SearchEvent.OnSearch)
            scope.launch {
                lazyListState.scrollToItem(0)
            }
            keyboardHide()
        }
    ) {
        places?.let { pagingItems ->
            if (pagingItems.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    modifier = Modifier.size(36.dp)
                )
            }
        } ?: Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search),
            modifier = Modifier.size(36.dp)
        )
    }
}