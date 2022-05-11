package com.nullpointer.newscompose.ui.screens.webView

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.ui.share.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun WebViewScreen(
    url: String,
    navigator: DestinationsNavigator,
) {
    val state = rememberWebViewState(url)
    val client = remember { AccompanistWebViewClient() }

    Scaffold(
        topBar = {
            ToolbarBack(title = stringResource(R.string.title_web_view_screen),
                iconNavigation = R.drawable.ic_clear,
                actionBack = navigator::popBackStack)
        }
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { client.navigator.reload() },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            WebView(
                state = state,
                client = client,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            )
        }
    }

}