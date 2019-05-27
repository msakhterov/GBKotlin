package ru.msakhterov.notesapp.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.ui.note.NoteActivity

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter{
            NoteActivity.start(this, it)
        }
        rv_notes.adapter = adapter


        viewModel.viewState().observe(this, Observer { viewState ->
            viewState?.let { adapter.notes = viewState.notes }
        })

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }
}
