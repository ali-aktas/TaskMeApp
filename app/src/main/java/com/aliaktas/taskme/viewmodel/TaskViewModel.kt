package com.aliaktas.taskme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.taskme.data.repository.DayRepository // Bu import TaskViewModel'de zaten vardı
import com.aliaktas.taskme.data.repository.TaskRepository
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
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                dayRepository.getAllDays(),
                taskRepository.getAllTasks()
            ) { days, tasks ->
                if (days.isEmpty()) {
                    val defaultDays = createDefaultDays() // Bu List<com.aliaktas.taskme.domain.model.Day> döndürür

                    //  İŞTE DEĞİŞİKLİK BURADA: .map { it.toEntity() } kısmını kaldırıyoruz
                    dayRepository.insertDays(defaultDays) // ÖNCEDEN: dayRepository.insertDays(defaultDays.map { it.toEntity() }) idi

                    _uiState.update { currentState ->
                        currentState.copy(
                            days = defaultDays, // ViewModel state'i domain modellerini tutar
                            tasks = tasks,
                            selectedDayId = defaultDays.firstOrNull()?.id ?: 0L
                        )
                    }
                } else {
                    // ... (mevcut kod)
                }
            }.collect()
        }
    }

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

    // --- YENİ FONKSİYONLAR ---

    // Görev Eylemleri için
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
            } else { // Başlık boşsa veya görev null ise dialogu kapat
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

    // Gün Görevlerini Temizleme Eylemleri için
    fun onDayCardLongPressed(dayId: Long) {
        if (_uiState.value.tasks.none { it.dayId == dayId }) return // O gün görev yoksa işlem yapma

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

    private fun createDefaultDays() = listOf(
        com.aliaktas.taskme.domain.model.Day(id = 1, name = "Pazartesi", order = 0),
        com.aliaktas.taskme.domain.model.Day(id = 2, name = "Salı", order = 1),
        com.aliaktas.taskme.domain.model.Day(id = 3, name = "Çarşamba", order = 2),
        com.aliaktas.taskme.domain.model.Day(id = 4, name = "Perşembe", order = 3),
        com.aliaktas.taskme.domain.model.Day(id = 5, name = "Cuma", order = 4),
        com.aliaktas.taskme.domain.model.Day(id = 6, name = "Cumartesi", order = 5),
        com.aliaktas.taskme.domain.model.Day(id = 7, name = "Pazar", order = 6)
    )

    // DayRepository'nin insertDays fonksiyonu DayEntity listesi bekliyor.
    private fun com.aliaktas.taskme.domain.model.Day.toEntity(): com.aliaktas.taskme.data.local.DayEntity {
        return com.aliaktas.taskme.data.local.DayEntity(
            id = this.id,
            name = this.name,
            order = this.order
        )
    }
}