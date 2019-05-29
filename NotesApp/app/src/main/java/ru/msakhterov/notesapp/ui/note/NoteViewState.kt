package ru.msakhterov.notesapp.ui.note

import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)