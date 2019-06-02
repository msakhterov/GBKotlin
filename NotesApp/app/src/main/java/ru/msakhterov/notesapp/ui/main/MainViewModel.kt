package ru.msakhterov.notesapp.ui.main

import android.arch.lifecycle.Observer
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.model.NoteResult
import ru.msakhterov.notesapp.ui.base.BaseViewModel

class MainViewModel: BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}