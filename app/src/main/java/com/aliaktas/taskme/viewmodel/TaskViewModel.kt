package com.aliaktas.taskme.viewmodel

import android.util.Log // Loglama için import ekle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.taskme.data.repository.DayRepository
import com.aliaktas.taskme.data.repository.TaskRepository
import com.aliaktas.taskme.domain.model.Day // Day domain modelini import et
import com.aliaktas.taskme.domain.model.Task
import com.aliaktas.taskme.ui.states.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dayRepository: DayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        Log.d("TaskViewModel", "ViewModel initialized, calling loadData.")
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            dayRepository.getAllDays().collectLatest { daysFromDb ->
                Log.d("TaskViewModel", "Observed daysFromDb: ${daysFromDb.size}")
                if (daysFromDb.isEmpty()) {
                    Log.d("TaskViewModel", "daysFromDb is empty. Creating default days.")
                    val defaultDays = createDefaultDays()
                    dayRepository.insertDays(defaultDays) // Bu işlem getAllDays flow'unu tekrar tetiklemeli
                    // _uiState'i burada defaultDays ile direkt güncellemek yerine,
                    // getAllDays flow'unun yeni veriyi yaymasını bekleyebiliriz.
                    // Ancak ilk açılışta hızlı göstermek için aşağıdaki güncelleme de bir strateji olabilir.
                    // Şimdilik, Flow'un doğal akışına bırakalım ve sadece insert yapalım.
                    // Eğer Flow hemen güncelleme yapmazsa, aşağıdaki update bloğunu tekrar değerlendirebiliriz.
                    // VEYA, UI state'ini hemen defaultDays ile set edip, Flow'dan gelen güncellemeyle birleşmesini sağlayabiliriz.
                    // İkinci bir combine ile tasks'ı da alalım.
                    _uiState.update { currentState ->
                        currentState.copy(
                            days = defaultDays, // Önce varsayılanlarla doldur
                            selectedDayId = defaultDays.firstOrNull()?.id ?: 0L
                        )
                    }
                    // Görevleri ayrıca toplayalım ve UI state'ine ekleyelim
                    taskRepository.getAllTasks().collect { tasksFromDb ->
                        Log.d("TaskViewModel", "Observed tasksFromDb with default days: ${tasksFromDb.size}")
                        _uiState.update { currentState ->
                            currentState.copy(
                                tasks = tasksFromDb
                                // days ve selectedDayId zaten yukarıda set edilmiş olmalı
                            )
                        }
                    }

                } else {
                    Log.d("TaskViewModel", "daysFromDb is NOT empty. Count: ${daysFromDb.size}")
                    // Günler zaten var, şimdi görevleri toplayıp UI state'ini güncelleyelim.
                    taskRepository.getAllTasks().collect { tasksFromDb ->
                        Log.d("TaskViewModel", "Observed tasksFromDb with existing days: ${tasksFromDb.size}")
                        _uiState.update { currentState ->
                            val currentSelectedDayId = currentState.selectedDayId
                            currentState.copy(
                                days = daysFromDb,
                                tasks = tasksFromDb,
                                selectedDayId = if (daysFromDb.any { it.id == currentSelectedDayId }) {
                                    currentSelectedDayId
                                } else {
                                    daysFromDb.firstOrNull()?.id ?: 0L
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // addTask, toggleTaskCompletion ve diğer fonksiyonlar aynı kalacak...
    // ... (önceki yanıtta verilen TaskViewModel fonksiyonları) ...


    fun addTask(title: String, dayId: Long) {
        viewModelScope.launch {
            if (title.isNotBlank() && dayId != 0L) {
                val newTask = Task(title = title, dayId = dayId)
                taskRepository.insertTask(newTask)
            }
        }
    }

    fun toggleTaskCompletion(taskId: Long) {
        viewModelScope.launch {
            val task = _uiState.value.tasks.find { it.id == taskId } ?: return@launch
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            taskRepository.updateTask(updatedTask)
        }
    }

    fun selectDay(dayId: Long) {
        _uiState.update { it.copy(selectedDayId = dayId) }
    }

    fun onTaskLongPressed(task: Task) {
        _uiState.update {
            it.copy(taskToTakeActionOn = task, showTaskActionDialog = true)
        }
    }

    fun dismissTaskActionDialog() {
        _uiState.update {
            it.copy(showTaskActionDialog = false, taskToTakeActionOn = null)
        }
    }

    fun requestDeleteTask() {
        _uiState.update { it.copy(showTaskActionDialog = false, showDeleteTaskConfirmDialog = true) }
    }

    fun confirmDeleteTask() {
        viewModelScope.launch {
            _uiState.value.taskToTakeActionOn?.let { task ->
                taskRepository.deleteTask(task)
                _uiState.update {
                    it.copy(
                        showDeleteTaskConfirmDialog = false,
                        taskToTakeActionOn = null
                    )
                }
            }
        }
    }

    fun dismissDeleteTaskConfirmDialog() {
        _uiState.update {
            it.copy(showDeleteTaskConfirmDialog = false, taskToTakeActionOn = null)
        }
    }

    fun requestEditTask() {
        _uiState.update {
            it.copy(
                showTaskActionDialog = false,
                showEditTaskDialog = true,
                taskToEditText = it.taskToTakeActionOn?.title ?: ""
            )
        }
    }

    fun onEditTaskTextChanged(newText: String) {
        _uiState.update { it.copy(taskToEditText = newText) }
    }

    fun confirmEditTask() {
        viewModelScope.launch {
            val currentTask = _uiState.value.taskToTakeActionOn
            val newTitle = _uiState.value.taskToEditText
            if (currentTask != null && newTitle.isNotBlank()) {
                val updatedTask = currentTask.copy(title = newTitle)
                taskRepository.updateTask(updatedTask)
                _uiState.update {
                    it.copy(
                        showEditTaskDialog = false,
                        taskToTakeActionOn = null,
                        taskToEditText = ""
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        showEditTaskDialog = false,
                        taskToTakeActionOn = null,
                        taskToEditText = ""
                    )
                }
            }
        }
    }

    fun dismissEditTaskDialog() {
        _uiState.update {
            it.copy(
                showEditTaskDialog = false,
                taskToTakeActionOn = null,
                taskToEditText = ""
            )
        }
    }

    fun onDayCardLongPressed(dayId: Long) {
        // O gün hiç tamamlanmamış görev yoksa bile menüyü gösterelim, belki de sadece o günü silmek istiyordur.
        // Veya sadece görev varsa gösterelim, bu UI kararına bağlı.
        // Şimdilik, günün var olması yeterli.
        if (_uiState.value.days.none{ it.id == dayId}) return

        _uiState.update {
            it.copy(dayIdToClearTasks = dayId, showClearDayTasksConfirmDialog = true)
        }
    }

    fun confirmClearDayTasks() {
        viewModelScope.launch {
            _uiState.value.dayIdToClearTasks?.let { dayId ->
                taskRepository.deleteTasksByDayId(dayId)
                _uiState.update {
                    it.copy(
                        showClearDayTasksConfirmDialog = false,
                        dayIdToClearTasks = null
                    )
                }
            }
        }
    }

    fun dismissClearDayTasksConfirmDialog() {
        _uiState.update {
            it.copy(showClearDayTasksConfirmDialog = false, dayIdToClearTasks = null)
        }
    }

    private fun createDefaultDays(): List<Day> = listOf( // Tipini List<Day> olarak belirtelim
        Day(id = 1, name = "Pazartesi", order = 0),
        Day(id = 2, name = "Salı", order = 1),
        Day(id = 3, name = "Çarşamba", order = 2),
        Day(id = 4, name = "Perşembe", order = 3),
        Day(id = 5, name = "Cuma", order = 4),
        Day(id = 6, name = "Cumartesi", order = 5),
        Day(id = 7, name = "Pazar", order = 6)
    )

    // Bu extension fonksiyon artık DayRepository içinde olduğu için ViewModel'de gereksiz.
    // Eğer DayRepository dışına da lazımsa (ki şu an değil gibi), ortak bir yere taşınabilir.
    /*
    private fun com.aliaktas.taskme.domain.model.Day.toEntity(): com.aliaktas.taskme.data.local.DayEntity {
        return com.aliaktas.taskme.data.local.DayEntity(
            id = this.id,
            name = this.name,
            order = this.order
        )
    }
    */
}