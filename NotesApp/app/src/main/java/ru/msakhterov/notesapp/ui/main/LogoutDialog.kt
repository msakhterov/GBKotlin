package ru.msakhterov.notesapp.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "_TAG"
        fun getInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        AlertDialog.Builder(context)
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Да") { dialog, whitch -> (activity as? LogoutListener)?.onLogout() }
            .setNegativeButton("Отмена") { dialog, whitch -> dismiss() }
            .create()

    interface LogoutListener {
        fun onLogout()
    }

}