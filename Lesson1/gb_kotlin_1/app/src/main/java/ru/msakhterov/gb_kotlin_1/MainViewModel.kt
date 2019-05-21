package ru.msakhterov.gb_kotlin_1

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val repo: Data = Data()
    private val viewStateLiveData: MutableLiveData<String> = MutableLiveData()

    fun viewState(): LiveData<String> = viewStateLiveData

    fun updateData(newData: String){
        repo.text = newData
        viewStateLiveData.value = repo.text
    }
}