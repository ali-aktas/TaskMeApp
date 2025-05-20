package com.aliaktas.taskme.data.local

import androidx.room.*
import com.aliaktas.taskme.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Görev veritabanı erişim nesnesi (DAO) - Room ile kullanılır
 * CRUD (Create, Read, Update, Delete) işlemlerini tanımlar
 */
@Dao
interface TaskDao {
    /**
     * Tüm görevleri veritabanından alır
     * Flow tipinde döndürülerek reaktif bir şekilde veri değişikliklerini izler
     *
     * @return Görevlerin listesi (Flow tipinde)
     */
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    /**
     * Belirli bir güne ait görevleri veritabanından alır
     *
     * @param dayId Görevleri alınacak günün ID'si
     * @return Belirtilen güne ait görevlerin listesi (Flow tipinde)
     */
    @Query("SELECT * FROM tasks WHERE dayId = :dayId")
    fun getTasksByDay(dayId: Long): Flow<List<TaskEntity>>

    /**
     * Yeni bir görev ekler
     *
     * @param task Eklenecek görev
     * @return Eklenen görevin otomatik oluşturulan ID'si
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    /**
     * Bir görevi günceller
     *
     * @param task Güncellenecek görev
     */
    @Update
    suspend fun updateTask(task: TaskEntity)

    /**
     * Bir görevi siler
     *
     * @param task Silinecek görev
     */
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    /**
     * Belirli bir ID'ye sahip görevi veritabanından alır
     *
     * @param id Alınacak görevin ID'si
     * @return Belirtilen ID'ye sahip görev
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    /**
     * Belirli bir güne ait tüm görevleri siler.
     *
     * @param dayId Silinecek görevlerin ait olduğu günün ID'si
     */
    @Query("DELETE FROM tasks WHERE dayId = :dayId") // YENİ FONKSİYON
    suspend fun deleteTasksByDayId(dayId: Long)
}