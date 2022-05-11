package com.nullpointer.newscompose.ui.screens.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.imageLoader
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.presentation.NewsViewModel
import com.nullpointer.newscompose.ui.screens.destinations.WebViewScreenDestination
import com.nullpointer.newscompose.ui.screens.empty.EmptyScreen
import com.nullpointer.newscompose.ui.screens.news.componets.ItemNew
import com.nullpointer.newscompose.ui.screens.news.componets.ItemNewShimmer
import com.nullpointer.newscompose.ui.share.OnBottomReached
import com.nullpointer.newscompose.ui.share.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val stateNews = newsViewModel.listNews.collectAsState()
    val listGridState = rememberLazyGridState()
    val context = LocalContext.current
    val messageNew = newsViewModel.messageNews
    val stateScaffold = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        messageNew.collect {
            stateScaffold.snackbarHostState.showSnackbar(context.getString(it))
        }
    }

    Scaffold(
        scaffoldState = stateScaffold,
        topBar = {
            ToolbarBack(title = stringResource(id = R.string.app_name))
        }
    ) { it ->
        val listNews = stateNews.value
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = newsViewModel.isRequested),
            onRefresh = {
                newsViewModel.requestLastNews()
                val imageLoader = context.imageLoader
                imageLoader.memoryCache.clear()
            },
            modifier = Modifier.padding(it)
        ) {
            when {
                listNews == null -> {
                    LazyVerticalGrid(columns = GridCells.Adaptive(250.dp)) {
                        items(10) {
                            ItemNewShimmer()
                        }
                    }
                }

                listNews.isEmpty() -> {
                    EmptyScreen(resourceRaw = R.raw.news,
                        emptyText = stringResource(R.string.message_empty_news))
                }

                listNews.isNotEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyVerticalGrid(columns = GridCells.Adaptive(250.dp),
                            state = listGridState) {
                            items(listNews.size,
                                key = { index -> listNews[index].id!! }) { index ->
                                ItemNew(
                                    new = listNews[index],
                                    modifier = Modifier.animateItemPlacement(),
                                    actionClick = {
                                        navigator.navigate(
                                            WebViewScreenDestination.invoke(it)
                                        )
                                    }
                                )
                            }
                        }
                        if (newsViewModel.isEnableConcatenate && !newsViewModel.isRequested)
                            listGridState.OnBottomReached(onLoadMore = newsViewModel::concatenateNews)
                        if (newsViewModel.isConcatenate)
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                    }
                }
            }
        }
    }
}