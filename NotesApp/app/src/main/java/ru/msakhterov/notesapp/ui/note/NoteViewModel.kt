package ru.msakhterov.notesapp.ui.note

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.annotation.VisibleForTesting
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.model.NoteResult
import ru.msakhterov.notesapp.ui.base.BaseViewModel

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = t.data as? Note))
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        }
    }
    private var repositoryNote: LiveData<NoteResult>? = null
    private val pendingNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun save(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    fun loadNote(noteId: String) {
        repositoryNote = notesRepository.getNoteById(noteId)
        repositoryNote?.observeForever(noteObserver)
    }

    fun deleteNote() {
        pendingNote?.let {
            notesRepository.deleteNote(it.id).observeForever { result ->
                result?.let {
                    viewStateLiveData.value = when (result) {
                        is NoteResult.Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                        is NoteResult.Error -> NoteViewState(error = result.error)
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let { note ->
            notesRepository.saveNote(note)
        }
        repositoryNote?.removeObserver(noteObserver)
    }

}