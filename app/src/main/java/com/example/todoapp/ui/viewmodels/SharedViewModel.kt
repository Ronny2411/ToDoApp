package com.example.todoapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.data.repositories.DataStoreRepository
import com.example.todoapp.data.repositories.ToDoRepository
import com.example.todoapp.util.Action
import com.example.todoapp.util.Constants.MAX_TITLE_LENGTH
import com.example.todoapp.util.RequestState
import com.example.todoapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    var id by mutableStateOf(0)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.LOW)
        private set

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set
    var searchTextState by mutableStateOf("")
        private set

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks : StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTasks : StateFlow<RequestState<List<ToDoTask>>> = _searchTasks

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState : StateFlow<RequestState<Priority>> = _sortState

    private val _selectedTask : MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask : StateFlow<ToDoTask?> = _selectedTask

    init {
        getAllTasks()
        readSortState()
    }

    private fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map {
                    Priority.valueOf(it)
                }.collect{
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (e : Exception){
            _sortState.value = RequestState.Error<Throwable>(e)
        }
    }

    fun persistSortState(priority: Priority){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    private fun getAllTasks(){
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e : Exception){
            _allTasks.value = RequestState.Error<Throwable>(e)
        }
    }

    fun getSelectedTask(taskId:Int){
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect{
                _selectedTask.value = it
            }
        }
    }

    fun searchDatabase(searchQuery : String){
        _searchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase("%$searchQuery%").collect{
                    searchedTasks->
                    _searchTasks.value = RequestState.Success(searchedTasks)
                }
            }
        } catch (e : Exception){
            _searchTasks.value = RequestState.Error<Throwable>(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(toDoTask = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
        searchTextState = ""
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(toDoTask = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
        searchTextState = ""
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(toDoTask = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
        searchTextState = ""
    }

    private fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun handleDatabaseAction(action: Action){
        when(action){
            Action.ADD->{
                addTask()
            }
            Action.UPDATE->{
                updateTask()
            }
            Action.DELETE->{
                deleteTask()
            }
            Action.DELETE_ALL->{
                deleteAllTasks()
            }
            Action.UNDO->{
                addTask()
            }
            else -> {

            }
        }
        this.action = Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask : ToDoTask?){
        if (selectedTask != null){
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitleLength(newTitle : String){
        if (newTitle.length < MAX_TITLE_LENGTH)
        title = newTitle
    }

    fun updateAction(newAction : Action){
        action = newAction
    }

    fun updateDescription(newDesc : String){
        description = newDesc
    }

    fun updatePriority(newPriority : Priority){
        priority = newPriority
    }

    fun updateSearchAppBarState(newSearchAppBarState: SearchAppBarState){
        searchAppBarState = newSearchAppBarState
    }

    fun updateSearchTextState(newSearchTextString: String){
        searchTextState = newSearchTextString
    }

    fun validateFields() : Boolean{
        return title.isNotEmpty() && description.isNotEmpty()
    }
}