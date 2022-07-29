package com.nullpointer.newscompose.ui.states

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class NewsScreenState(
    val context: Context,
    val scaffoldState: ScaffoldState,
    val lazyGridState: LazyGridState,
    val swipeState: SwipeRefreshState
) {
    suspend fun showSnackMessage(@StringRes stringRes: Int) {
        scaffoldState.snackbarHostState.showSnackbar(
            context.getString(stringRes)
        )
    }
}

@Composable
fun rememberNewsScreenState(
    isRefreshing: Boolean,
    context: Context = LocalContext.current,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    swipeState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
) = remember(scaffoldState,lazyGridState,swipeState) {
    NewsScreenState(context,scaffoldState,lazyGridState,swipeState)
}