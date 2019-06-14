package ru.msakhterov.notesapp.data.provider

import android.arch.lifecycle.LiveData
import kotlinx.coroutines.channels.ReceiveChannel
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.entity.User
import ru.msakhterov.notesapp.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(id: String)
}