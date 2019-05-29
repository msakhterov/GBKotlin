package ru.msakhterov.notesapp.ui.main

import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)