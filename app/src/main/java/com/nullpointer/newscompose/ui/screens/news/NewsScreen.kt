package com.nullpointer.newscompose.ui.screens.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.core.utils.Resource
import com.nullpointer.newscompose.models.NewsDB
import com.nullpointer.newscompose.presentation.NewsViewModel
import com.nullpointer.newscompose.ui.screens.destinations.WebViewScreenDestination
import com.nullpointer.newscompose.ui.screens.empty.EmptyScreen
import com.nullpointer.newscompose.ui.screens.news.componets.ItemNew
import com.nullpointer.newscompose.ui.screens.news.componets.ItemNewShimmer
import com.nullpointer.newscompose.ui.share.CircularProgressAnimation
import com.nullpointer.newscompose.ui.share.OnBottomReached
import com.nullpointer.newscompose.ui.share.ToolbarBack
import com.nullpointer.newscompose.ui.states.NewsScreenState
import com.nullpointer.newscompose.ui.states.rememberNewsScreenState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun NewsScreen(
    navigator: DestinationsNavigator,
    newsViewModel: NewsViewModel = hiltViewModel(),
    newsScreenState: NewsScreenState = rememberNewsScreenState(
        isRefreshing = newsViewModel.isRequested,
        sizeScrollMore = 50f
    )
) {
    val stateNews by newsViewModel.listNews.collectAsState()

    LaunchedEffect(key1 = Unit) {
        newsViewModel.messageNews.collect(newsScreenState::showSnackMessage)
    }

    Scaffold(
        scaffoldState = newsScreenState.scaffoldState,
        topBar = { ToolbarBack(title = stringResource(id = R.string.app_name)) }
    ) { paddingValues ->
        SwipeRefresh(
            state = newsScreenState.swipeState,
            onRefresh = newsViewModel::requestLastNews,
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val stateNews = stateNews) {
                is Resource.Success -> {
                    if (stateNews.data.isEmpty()) {
                        EmptyScreen(
                            resourceRaw = R.raw.news,
                            emptyText = stringResource(R.string.message_empty_news),
                            modifier = Modifier.padding(paddingValues)
                        )
                    } else {
                        ListNews(
                            listNews = stateNews.data,
                            modifier = Modifier.padding(paddingValues),
                            isConcatenate = newsViewModel.isConcatenate,
                            listGridState = newsScreenState.lazyGridState,
                            requestMoreNews = {
                                newsViewModel.concatenateNews(newsScreenState::animateScrollMore)
                            },
                            clickNew = { navigator.navigate(WebViewScreenDestination(it)) },
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(250.dp),
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(10, key = { it }) {
                            ItemNewShimmer()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListNews(
    listGridState: LazyGridState,
    listNews: List<NewsDB>,
    isConcatenate: Boolean,
    requestMoreNews: () -> Unit,
    clickNew: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(250.dp),
            state = listGridState
        ) {
            items(listNews, key = { it.id }) { new ->
                ItemNew(
                    new = new,
                    modifier = Modifier.animateItemPlacement(),
                    actionClick = clickNew
                )
            }
        }
        listGridState.OnBottomReached(onLoadMore = requestMoreNews)
        CircularProgressAnimation(
            isVisible = isConcatenate,
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        )
    }
}

