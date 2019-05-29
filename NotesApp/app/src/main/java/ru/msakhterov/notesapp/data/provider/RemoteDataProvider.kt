package ru.msakhterov.notesapp.data.provider

import android.arch.lifecycle.LiveData
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}