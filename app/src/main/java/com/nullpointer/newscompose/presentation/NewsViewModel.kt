package com.nullpointer.newscompose.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.core.delegates.SavableComposeState
import com.nullpointer.newscompose.core.utils.InternetCheckError
import com.nullpointer.newscompose.core.utils.ServerTimeOut
import com.nullpointer.newscompose.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo: NewsRepository,
    stateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_IS_ENABLE_CONCATENATE = "KEY_IS_ENABLE_CONCATENATE"
        private const val KEY_NUMBER_PAGER = "KEY_NUMBER_PAGER"
    }

    private val _messageNews = Channel<Int>()
    val messageNews = _messageNews.receiveAsFlow()

    private var jobRequestNews: Job? = null
    private var jobConcatenate: Job? = null

    var isRequested by mutableStateOf(false)
        private set

    var isConcatenate by mutableStateOf(false)
        private set

    var isEnableConcatenate by SavableComposeState(stateHandle, KEY_IS_ENABLE_CONCATENATE, true)
        private set

    var numberPager by SavableComposeState(stateHandle, KEY_NUMBER_PAGER, 1)
        private set

    val listNews = newsRepo.listNews.catch {
        Timber.e("Error al obtener la lista de noticias $it")
        emit(emptyList())
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null)


    init {
        requestLastNews()
    }

    fun requestLastNews() {
        jobRequestNews?.cancel()
        jobRequestNews = viewModelScope.launch {
            isRequested = true
            try {
                val numberNews = withContext(Dispatchers.IO) { newsRepo.requestNews("mx") }
                Timber.d("Se obtuvieron $numberNews noticia(s) al primer request")
                isEnableConcatenate = true
                numberPager = 1

            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is InternetCheckError -> _messageNews.trySend(R.string.error_network)
                    is ServerTimeOut -> _messageNews.trySend(R.string.server_time_out)
                    is NullPointerException -> _messageNews.trySend(R.string.server_response_null)
                    else -> {
                        Timber.e("Error desconocido en la peticion $e")
                        _messageNews.trySend(R.string.error_unknown)
                    }
                }
            } finally {
                isRequested = false
            }
        }
    }


    fun concatenateNews() {
        jobConcatenate?.cancel()
        jobConcatenate = viewModelScope.launch {
            isConcatenate = true
            try {
                numberPager++
                val numberNews = withContext(Dispatchers.IO) {
                    newsRepo.concatenateNews("mx", numberPager)
                }
                Timber.d("Se obtuvieron $numberNews noticia(s) nuevas")
                if (numberNews == 0) isEnableConcatenate = false
            } catch (e: Exception) {
                numberPager--
                when (e) {
                    is CancellationException -> throw e
                    is InternetCheckError -> _messageNews.trySend(R.string.error_network)
                    is ServerTimeOut -> _messageNews.trySend(R.string.server_time_out)
                    is NullPointerException -> _messageNews.trySend(R.string.server_response_null)
                    else -> {
                        Timber.e("Error desconocido en la peticion $e")
                        _messageNews.trySend(R.string.error_unknown)
                    }
                }
            } finally {
                isConcatenate = false
            }
        }
    }

}