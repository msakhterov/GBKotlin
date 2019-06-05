package ru.msakhterov.notesapp.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.common.format
import ru.msakhterov.notesapp.common.getColorInt
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseActivity
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                noteId?.let { putExtra(EXTRA_NOTE, noteId) }
            }
            context.startActivity(intent)
        }
    }

    private var color: Note.Color = Note.Color.WHITE
    private var note: Note? = null
    override val layoutRes: Int = R.layout.activity_note
    override val viewModel: NoteViewModel by viewModel()

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.note_new_note)
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) {
            finish()
            return
        }
        this.note = data.note
        supportActionBar?.title = if (this.note != null) {
            note!!.lastChanged.format(DATE_TIME_FORMAT)
        } else {
            getString(R.string.note_new_note)
        }
        initView()
    }

    private fun initView() {
        note?.let { note ->
            removeTextListener()
            et_title.setText(note.title)
            et_text.setText(note.text)
            et_title.setSelection(et_title.text?.length ?: 0)
            et_text.setSelection(et_text.text?.length ?: 0)
            color = note.color
            toolbar.setBackgroundColor(note.color.getColorInt(this))
        }
        setTextListener()

        colorPicker.onColorClickListener = {
            color = it
            toolbar.setBackgroundColor(color.getColorInt(this))
            saveNote()
        }
    }

    private fun setTextListener() {
        et_title.addTextChangedListener(textWatcher)
        et_text.addTextChangedListener(textWatcher)
    }

    private fun removeTextListener() {
        et_title.removeTextChangedListener(textWatcher)
        et_text.removeTextChangedListener(textWatcher)
    }

    private fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return
        note = note?.copy(
            title = et_title.text.toString(),
            text = et_text.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: createNewNote()
        note?.let { viewModel.save(it) }
    }

    private fun createNewNote() = Note(UUID.randomUUID().toString(), et_title.text.toString(), et_text.toString())

    override fun onCreateOptionsMenu(menu: Menu) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.note_delete -> deleteNote().let { true }
        R.id.note_select_color -> togglePalette().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.cancel_btn_title) { dialog -> dialog.dismiss() }
            positiveButton(R.string.ok_btn_title) { viewModel.deleteNote() }
        }.show()
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

}
