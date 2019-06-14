package ru.msakhterov.notesapp.ui.main

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.model.NoteResult
import ru.msakhterov.notesapp.ui.base.BaseViewModel

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainViewModel(notesRepository: NotesRepository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = notesRepository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is NoteResult.Success<*> -> setData(it.data as? List<Note>)
                    is NoteResult.Error -> setError(it.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}