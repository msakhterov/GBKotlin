package ru.msakhterov.notesapp.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.msakhterov.notesapp.data.NotesRepository
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.model.NoteResult
import ru.msakhterov.notesapp.ui.note.NoteViewModel

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val loadNoteLiveData = MutableLiveData<NoteResult>()
    private val deleteNoteLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: NoteViewModel

//    @Before
//    fun setUp() {
//        clearMocks(mockRepository)
//        every { mockRepository.getNoteById(any()) } returns loadNoteLiveData
//        every { mockRepository.deleteNote(any()) } returns deleteNoteLiveData
//        viewModel = NoteViewModel(mockRepository)
//        viewModel.loadNote(Note(id = "1").id)
//    }
//
//    @Test
//    fun `should call getNoteById once`() {
//        verify(exactly = 1) { mockRepository.getNoteById(any()) }
//    }
//
//    @Test
//    fun `loadNote should return Note`() {
//        var result: Note? = null
//        val testData = Note(id = "1")
//        loadNoteLiveData.value = NoteResult.Success(testData)
//        viewModel.getViewState().observeForever{
//            result = it?.data?.note
//        }
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `loadNote should return error`() {
//        var result: Throwable? = null
//        val testData = Throwable("error")
//
//        loadNoteLiveData.value = NoteResult.Error(testData)
//        viewModel.getViewState().observeForever { result = it?.error }
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `saveNote should change handling Note`() {
//        var result: Note? = null
//        val testData = Note(id = "2")
//        viewModel.getViewState().observeForever{
//            result = it?.data?.note
//        }
//        loadNoteLiveData.value = NoteResult.Success(Note(id = "1"))
//        viewModel.save(testData)
//
//        assertEquals(testData, result)
//    }
//
//
//    @Test
//    fun `deleteNote should mark Note as deleted`() {
//        var result: Boolean? = null
//        val testData = true
//        viewModel.getViewState().observeForever{
//            result = it?.data?.isDeleted
//        }
//        loadNoteLiveData.value = NoteResult.Success(Note(id = "1"))
//        deleteNoteLiveData.value = NoteResult.Success(null)
//        viewModel.deleteNote()
//
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `deleteNote should return error`() {
//        var result: Throwable? = null
//        val testData = Throwable("error")
//        loadNoteLiveData.value = NoteResult.Success(Note(id = "1"))
//        deleteNoteLiveData.value = NoteResult.Error(testData)
//        viewModel.deleteNote()
//        viewModel.getViewState().observeForever { result = it?.error }
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `should remove observer`() {
//        viewModel.onCleared()
//        assertFalse(loadNoteLiveData.hasObservers())
//    }

}