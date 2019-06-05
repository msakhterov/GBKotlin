package ru.msakhterov.notesapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.msakhterov.notesapp.R
import ru.msakhterov.notesapp.data.entity.Note
import ru.msakhterov.notesapp.ui.base.BaseActivity
import ru.msakhterov.notesapp.ui.note.NoteActivity
import ru.msakhterov.notesapp.ui.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_main
    private lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter {
            NoteActivity.start(this, it.id)
        }
        rv_notes.adapter = adapter
        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_dialog_title
            messageResource = R.string.logout_dialog_message
            positiveButton(R.string.ok_btn_title) { onLogout() }
            negativeButton(R.string.cancel_btn_title) {dialog -> dialog.dismiss() }
        }
    }

    private fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

}
