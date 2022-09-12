package com.nullpointer.newscompose.ui.states

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NewsScreenState(
    val context: Context,
    val scaffoldState: ScaffoldState,
    val lazyGridState: LazyGridState,
    val swipeState: SwipeRefreshState,
    private val sizeScrollMore:Float,
    private val coroutineScope: CoroutineScope,
) {


    suspend fun showSnackMessage(@StringRes stringRes: Int) {
        scaffoldState.snackbarHostState.showSnackbar(
            context.getString(stringRes)
        )
    }

    fun animateScrollMore(){
        coroutineScope.launch {
            lazyGridState.animateScrollBy(sizeScrollMore)
        }
    }
}

@Composable
fun rememberNewsScreenState(
    isRefreshing: Boolean,
    sizeScrollMore: Float,
    context: Context = LocalContext.current,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lazyGridState: LazyGridState = rememberLazyGridState(),
    coroutineScope: CoroutineScope= rememberCoroutineScope(),
    swipeState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
) = remember(scaffoldState,lazyGridState,swipeState,coroutineScope) {
    NewsScreenState(context, scaffoldState, lazyGridState, swipeState, sizeScrollMore, coroutineScope)
}