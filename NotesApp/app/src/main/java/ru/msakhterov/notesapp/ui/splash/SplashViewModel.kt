package ru.msakhterov.notesapp.ui.splash

import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.error.NoAuthException
import ru.msakhterov.notesapp.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }

}