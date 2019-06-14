package ru.msakhterov.notesapp.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.ajalt.timberkt.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.data.entity.User
import ru.msakhterov.notesapp.data.error.NoAuthException
import ru.msakhterov.notesapp.model.NoteResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val NOTES_COLLECTION = "notes"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let {
            User(it.displayName ?: "", it.email ?: "") })
    }

    private fun getUserNotesCollection() = currentUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    @ExperimentalCoroutinesApi
    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
        Channel<NoteResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null

            try {
                registration = getUserNotesCollection().addSnapshotListener { snapshot, e ->
                    val value = e?.let { NoteResult.Error(e) }
                        ?: snapshot?.let {
                            val notes = it.documents.map { d ->
                                d.toObject(Note::class.java)
                            }
                            NoteResult.Success(notes)
                        }
                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(NoteResult.Error(e))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String) : Note = suspendCoroutine { continuation ->
        try {
            getUserNotesCollection()
                .document(id)
                .get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(snapshot.toObject(Note::class.java)!!)
                }.addOnFailureListener {
                    Timber.d { "Error getting note by id $id, message: ${it.message}" }
                    throw it
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun saveNote(note: Note) : Note = suspendCoroutine { continuation ->
        try {
            getUserNotesCollection()
                .document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Timber.d { "Note $note is saved" }
                    continuation.resume(note)
                }.addOnFailureListener {
                    Timber.d { "Error saving note $note, message: ${it.message}" }
                    throw it
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        getUserNotesCollection().document(id).delete()
            .addOnSuccessListener {
                continuation.resume(Unit)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

}