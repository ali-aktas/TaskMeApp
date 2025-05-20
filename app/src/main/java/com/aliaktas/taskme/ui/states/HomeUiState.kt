package com.aliaktas.taskme.ui.states

import com.aliaktas.taskme.domain.model.Day
import com.aliaktas.taskme.domain.model.Task

data class HomeUiState(
    val days: List<Day> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val selectedDayId: Long = 0,

    // Yeni state'ler
    val taskToTakeActionOn: Task? = null, // Üzerinde işlem yapılacak görev (silme/düzenleme)
    val showTaskActionDialog: Boolean = false, // Görev eylem (düzenle/sil) menüsünü göster
    val showEditTaskDialog: Boolean = false, // Görev düzenleme dialogunu göster
    val taskToEditText: String = "", // Düzenlenen görev için geçici metin

    val dayIdToClearTasks: Long? = null, // Görevleri temizlenecek günün ID'si
    val showClearDayTasksConfirmDialog: Boolean = false, // Gün görevlerini temizleme onay dialogu
    val showDeleteTaskConfirmDialog: Boolean = false // Tek görev silme onay dialogu
) {
    val selectedDay: Day? = days.find { it.id == selectedDayId }
    val filteredTasks: List<Task> = tasks.filter { it.dayId == selectedDayId }
}