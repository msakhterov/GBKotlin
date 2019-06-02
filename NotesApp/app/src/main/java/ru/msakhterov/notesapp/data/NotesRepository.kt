package ru.msakhterov.notesapp.data

import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.provider.FireStoreProvider
import ru.msakhterov.notesapp.data.provider.RemoteDataProvider

object NotesRepository {

    private val remoteProvider : RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

}