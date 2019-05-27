package ru.msakhterov.notesapp.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.msakhterov.notesapp.data.NotesRepository

class MainViewModel: ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        NotesRepository.getNotesLiveData().observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it!!) ?: MainViewState(it!!)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}