package com.aliaktas.taskme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.taskme.data.repository.DayRepository
import com.aliaktas.taskme.data.repository.TaskRepository
import com.aliaktas.taskme.domain.model.Task
import com.aliaktas.taskme.ui.states.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.Typography.dagger

/**
 * Ana ekran için ViewModel
 * Görev ve gün verilerini yönetir ve UI state'ini günceller
 *
 * @param taskRepository Görev veritabanı işlemleri için repository
 * @param dayRepository Gün veritabanı işlemleri için repository
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dayRepository: DayRepository
) : ViewModel() {

    // UI durumunu tutacak MutableStateFlow
    private val _uiState = MutableStateFlow(HomeUiState())

    // Dışarıya sadece okuma izni verilen UI durumu
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // ViewModel oluşturulduğunda verileri yükle
        loadData()
    }

    /**
     * Görev ve gün verilerini veritabanından yükler
     */
    private fun loadData() {
        viewModelScope.launch {
            // Eşzamanlı olarak günleri ve görevleri yükle
            combine(
                dayRepository.getAllDays(),
                taskRepository.getAllTasks()
            ) { days, tasks ->
                // Eğer hiç gün yoksa varsayılan günleri oluştur
                if (days.isEmpty()) {
                    val defaultDays = createDefaultDays()
                    dayRepository.insertDays(defaultDays)
                    _uiState.update { it.copy(
                        days = defaultDays,
                        selectedDayId = defaultDays.firstOrNull()?.id ?: 0
                    ) }
                } else {
                    // Günler ve görevlerle UI state'ini güncelle
                    _uiState.update { it.copy(
                        days = days,
                        tasks = tasks,
                        selectedDayId = it.selectedDayId.takeIf { id -> id != 0L } ?: days.firstOrNull()?.id ?: 0
                    ) }
                }
            }.collect()
        }
    }

    /**
     * Yeni bir görev ekler
     *
     * @param title Görev başlığı
     * @param dayId Görevin ekleneceği günün ID'si
     */
    fun addTask(title: String, dayId: Long) {
        viewModelScope.launch {
            val newTask = Task(
                title = title,
                dayId = dayId
            )
            taskRepository.insertTask(newTask)
        }
    }

    /**
     * Görevin tamamlanma durumunu değiştirir
     *
     * @param taskId Güncellenecek görevin ID'si
     */
    fun toggleTaskCompletion(taskId: Long) {
        viewModelScope.launch {
            // Mevcut görevi bul
            val task = _uiState.value.tasks.find { it.id == taskId } ?: return@launch

            // Tamamlanma durumunu ters çevir
            val updatedTask = task.copy(isCompleted = !task.isCompleted)

            // Güncellenen görevi veritabanına kaydet
            taskRepository.updateTask(updatedTask)
        }
    }

    /**
     * Seçili günü değiştirir
     *
     * @param dayId Seçilecek günün ID'si
     */
    fun selectDay(dayId: Long) {
        _uiState.update { it.copy(selectedDayId = dayId) }
    }

    /**
     * Varsayılan günleri oluşturur
     *
     * @return Varsayılan gün listesi
     */
    private fun createDefaultDays() = listOf(
        com.aliaktas.taskme.domain.model.Day(id = 1, name = "Pazartesi", order = 0),
        com.aliaktas.taskme.domain.model.Day(id = 2, name = "Salı", order = 1),
        com.aliaktas.taskme.domain.model.Day(id = 3, name = "Çarşamba", order = 2),
        com.aliaktas.taskme.domain.model.Day(id = 4, name = "Perşembe", order = 3),
        com.aliaktas.taskme.domain.model.Day(id = 5, name = "Cuma", order = 4),
        com.aliaktas.taskme.domain.model.Day(id = 6, name = "Cumartesi", order = 5),
        com.aliaktas.taskme.domain.model.Day(id = 7, name = "Pazar", order = 6)
    )
}