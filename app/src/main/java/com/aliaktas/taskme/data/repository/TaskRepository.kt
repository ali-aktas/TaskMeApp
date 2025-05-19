package com.aliaktas.taskme.data.repository

import com.aliaktas.taskme.data.local.TaskDao
import com.aliaktas.taskme.data.local.TaskEntity
import com.aliaktas.taskme.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Görev verilerini yöneten repository sınıfı
 * ViewModel ile veritabanı arasında köprü görevi görür
 *
 * @param taskDao Görev veritabanı erişim nesnesi
 */
@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    /**
     * Tüm görevleri veritabanından alır ve domain modeline dönüştürür
     *
     * @return Görevlerin domain model listesi (Flow tipinde)
     */
    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { taskEntities ->
            taskEntities.map { it.toDomainModel() }
        }
    }

    /**
     * Belirli bir güne ait görevleri alır
     *
     * @param dayId Görevleri alınacak günün ID'si
     * @return Belirtilen güne ait görevlerin domain model listesi (Flow tipinde)
     */
    fun getTasksByDay(dayId: Long): Flow<List<Task>> {
        return taskDao.getTasksByDay(dayId).map { taskEntities ->
            taskEntities.map { it.toDomainModel() }
        }
    }

    /**
     * Yeni bir görev ekler
     *
     * @param task Eklenecek görev
     * @return Eklenen görevin ID'si
     */
    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity())
    }

    /**
     * Bir görevi günceller
     *
     * @param task Güncellenecek görev
     */
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    /**
     * Bir görevi siler
     *
     * @param task Silinecek görev
     */
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    /**
     * Belirli bir ID'ye sahip görevi alır
     *
     * @param id Alınacak görevin ID'si
     * @return İstenen görev veya null
     */
    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)?.toDomainModel()
    }

    /**
     * Task domain modelini TaskEntity veritabanı modeline dönüştürür
     *
     * @return TaskEntity veritabanı modeli
     */
    private fun Task.toEntity(): TaskEntity {
        return TaskEntity(
            id = this.id,
            title = this.title,
            dayId = this.dayId,
            isCompleted = this.isCompleted
        )
    }

    /**
     * TaskEntity veritabanı modelini Task domain modeline dönüştürür
     *
     * @return Task domain modeli
     */
    private fun TaskEntity.toDomainModel(): Task {
        return Task(
            id = this.id,
            title = this.title,
            dayId = this.dayId,
            isCompleted = this.isCompleted
        )
    }
}