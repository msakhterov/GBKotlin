package ru.msakhterov.notesapp.ui.splash

import kotlinx.coroutines.launch
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.error.NoAuthException
import ru.msakhterov.notesapp.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }

}