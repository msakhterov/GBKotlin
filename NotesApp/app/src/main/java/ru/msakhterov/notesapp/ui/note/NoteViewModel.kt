package ru.msakhterov.notesapp.ui.note

import android.support.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseViewModel

@ExperimentalCoroutinesApi
class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<NoteData>() {

    private val pendingNote: Note?
        get() = getViewState().poll()?.note

    fun save(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                notesRepository.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                pendingNote?.let { notesRepository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let { notesRepository.saveNote(it) }
            super.onCleared()
        }
    }

}