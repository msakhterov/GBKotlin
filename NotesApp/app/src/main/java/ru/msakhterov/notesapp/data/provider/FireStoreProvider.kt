package ru.msakhterov.notesapp.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.model.NoteResult
import com.github.ajalt.timberkt.Timber

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                result.value = NoteResult.Error(e)
            } else if (snapshot != null) {
                val notes = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get().addOnSuccessListener { snapshot ->
            result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
        }.addOnFailureListener { e ->
            result.value = NoteResult.Error(e)
        }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id)
            .set(note).addOnSuccessListener {
                Timber.d { "Note $note is saved" }
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {
                Timber.d {"Error saving note $note, message: ${it.message}"}
                result.value = NoteResult.Error(it)
            }
        return result
    }

}