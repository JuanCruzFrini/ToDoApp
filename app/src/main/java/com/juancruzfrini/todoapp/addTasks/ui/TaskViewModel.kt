package com.juancruzfrini.todoapp.addTasks.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juancruzfrini.todoapp.addTasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(): ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog : LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks :List<TaskModel> = _tasks

    fun onDialogClose(){
        _showDialog.value = false
    }

    fun onTaskCreated(task: String) {
        _showDialog.value = false
        _tasks.add(TaskModel(task = task))
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckboxSelected(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)
        //_tasks.remove(taskModel)
    }

}