package ru.msakhterov.notesapp.ui.note

import ru.msakhterov.notesapp.data.entity.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
