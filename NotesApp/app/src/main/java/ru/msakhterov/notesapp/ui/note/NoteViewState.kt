package ru.msakhterov.notesapp.ui.note

import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error){
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}