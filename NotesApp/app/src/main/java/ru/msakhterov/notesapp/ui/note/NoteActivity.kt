package ru.msakhterov.notesapp.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.common.format
import ru.msakhterov.notesapp.common.getColorInt
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

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

    private var note: Note? = null
    override val layoutRes: Int = R.layout.activity_note
    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of( this).get(NoteViewModel::class.java)
    }

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
        } ?:let {
            supportActionBar?.title = getString(R.string.note_new_note)
        }
    }

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = if(this.note != null) {
            note!!.lastChanged.format(DATE_TIME_FORMAT)
        } else {
            getString(R.string.note_new_note)
        }
        initView()
    }

    private fun initView() {
        note?.let { note ->
            et_title.setText(note.title)
            et_text.setText(note.text)
            toolbar.setBackgroundColor(note.color.getColorInt(this))
        }
        et_title.addTextChangedListener(textWatcher)
        et_text.addTextChangedListener(textWatcher)
    }

    private fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return
        note = note?.copy(
            title = et_title.text.toString(),
            text = et_text.text.toString(),
            lastChanged = Date()
        ) ?: createNewNote()
        note?.let { viewModel.save(it) }
    }

    fun createNewNote() = Note(UUID.randomUUID().toString(), et_title.text.toString(), et_text.toString())

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        } else -> super.onOptionsItemSelected(item)
    }

}
