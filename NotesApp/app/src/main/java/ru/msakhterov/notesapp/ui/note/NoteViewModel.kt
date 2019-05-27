package ru.msakhterov.notesapp.ui.note

import android.arch.lifecycle.ViewModel
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note

class NoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let { note ->
            NotesRepository.saveNote(note)
        }
    }

}