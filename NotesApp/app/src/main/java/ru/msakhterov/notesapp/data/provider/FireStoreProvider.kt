package ru.msakhterov.notesapp.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.ajalt.timberkt.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.entity.User
import ru.msakhterov.notesapp.data.error.NoAuthException
import ru.msakhterov.notesapp.model.NoteResult

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val NOTES_COLLECTION = "notes"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }

    private fun getUserNotesCollection() = currentUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes() = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().addSnapshotListener { snapshot, e ->
                value = e?.let {
                    throw it
                } ?: snapshot?.let {
                    val notes = it.documents.map { it.toObject(Note::class.java) }
                    NoteResult.Success(notes)
                }
            }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection()
                .document(id)
                .get()
                .addOnSuccessListener { snapshot ->
                    value = NoteResult.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener {
                    Timber.d { "Error getting note by id $id, message: ${it.message}" }
                    throw it
                }

        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection()
                .document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Timber.d { "Note $note is saved" }
                    value = NoteResult.Success(note)
                }.addOnFailureListener {
                    Timber.d { "Error saving note $note, message: ${it.message}" }
                    throw it
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun deleteNote(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        getUserNotesCollection().document(id).delete()
            .addOnSuccessListener {
                value = NoteResult.Success(null)
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }

}