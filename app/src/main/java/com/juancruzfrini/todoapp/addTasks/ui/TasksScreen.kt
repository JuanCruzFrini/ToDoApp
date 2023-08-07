package com.juancruzfrini.todoapp.addTasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.juancruzfrini.todoapp.addTasks.ui.model.TaskModel

//@Preview
@Composable
fun TasksScreen(viewModel: TaskViewModel) {

    val showDialog by viewModel.showDialog.observeAsState(false)

    Box(modifier = Modifier.fillMaxSize()){
        FabDialog(Modifier.align(Alignment.BottomEnd), viewModel)
        AddTasksDialog(
            show = showDialog,
            onDismiss = { viewModel.onDialogClose() },
            onTaskAdded =  { viewModel.onTaskCreated(it) }
        )
        TasksList(viewModel)
    }
}

@Composable
fun TasksList(viewModel: TaskViewModel) {

    val myTasks:List<TaskModel> = viewModel.tasks

    LazyColumn {
        items(myTasks, key = { it.id }){
            TaskItem(it, viewModel)
        }
    }
}

//@Preview
@Composable
fun TaskItem(taskModel: TaskModel, viewModel: TaskViewModel){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    viewModel.onItemRemove(taskModel)
                })
            }
        ,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = taskModel.task, modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp))
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = {
                    viewModel.onCheckboxSelected(taskModel)
                })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, viewModel: TaskViewModel) {
    FloatingActionButton(
        onClick = { viewModel.onShowDialogClick() },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun AddTasksDialog(show:Boolean, onDismiss:() -> Unit, onTaskAdded:(String) -> Unit){
    var myTask by rememberSaveable { mutableStateOf("") }

    if (show){
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(text = "Añadir tarea", color = Color.LightGray, fontSize = 16.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = {myTask = it} )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Enviar tarea")
                }
            }
        }
    }
}
