package com.nullpointer.newscompose.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.newscompose.core.delegates.SavableComposeState
import com.nullpointer.newscompose.core.delegates.SavableProperty
import com.nullpointer.newscompose.core.utils.ExceptionManager
import com.nullpointer.newscompose.core.utils.Resource
import com.nullpointer.newscompose.core.utils.launchSafeIO
import com.nullpointer.newscompose.domain.NewsRepository
import com.nullpointer.newscompose.models.NewsDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
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

    private var isEnableConcatenate by SavableProperty(stateHandle, KEY_IS_ENABLE_CONCATENATE, true)

    var numberPager by SavableProperty(stateHandle, KEY_NUMBER_PAGER, 1)
        private set

    val listNews = flow<Resource<List<NewsDB>>> {
        newsRepo.listNews.collect{
            emit(Resource.Success(it))
            isEnableConcatenate = true
        }
    }.catch {
        Timber.e("Error get news from database $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Failure)


    init {
        requestLastNews()
    }

    fun requestLastNews() {
        jobRequestNews?.cancel()
        jobRequestNews = launchSafeIO(
            blockBefore = { isRequested = true },
            blockAfter = { isRequested = false },
            blockIO = {
                val numberNews = newsRepo.requestNews("mx")
                Timber.d("Se obtuvieron $numberNews noticia(s) al primer request")
                withContext(Dispatchers.Main) {
                    isEnableConcatenate = true
                    numberPager = 1
                }
            },
            blockException = {
                val message = ExceptionManager.getMessageForException(it, "Exception get news")
                _messageNews.trySend(message)
            }
        )
    }


    fun concatenateNews(
        callbackSuccess: () -> Unit
    ) {
        jobConcatenate?.cancel()
        if (isEnableConcatenate)
            jobRequestNews = launchSafeIO(
                blockBefore = { isConcatenate = true },
                blockAfter = { isConcatenate = false },
                blockIO = {
                    val numberNews = newsRepo.concatenateNews("mx", numberPager + 1)
                    Timber.d("Se obtuvieron $numberNews noticia(s) nuevas")
                    withContext(Dispatchers.Main) {
                        numberPager++
                        if (numberNews == 0) isEnableConcatenate = false
                        callbackSuccess()
                    }
                },
                blockException = {
                    val message =
                        ExceptionManager.getMessageForException(
                            it,
                            "Exception get concatenate news"
                        )
                    _messageNews.trySend(message)
                }
            )
    }

}