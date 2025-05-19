package com.aliaktas.taskme.data.local

import androidx.room.*
import com.aliaktas.taskme.data.local.DayEntity
import kotlinx.coroutines.flow.Flow

/**
 * Gün veritabanı erişim nesnesi (DAO) - Room ile kullanılır
 * CRUD (Create, Read, Update, Delete) işlemlerini tanımlar
 */
@Dao
interface DayDao {
    /**
     * Tüm günleri veritabanından alır
     * Flow tipinde döndürülerek reaktif bir şekilde veri değişikliklerini izler
     *
     * @return Günlerin listesi (Flow tipinde)
     */
    @Query("SELECT * FROM days ORDER BY `order` ASC")
    fun getAllDays(): Flow<List<DayEntity>>

    /**
     * Yeni bir gün ekler
     *
     * @param day Eklenecek gün
     * @return Eklenen günün otomatik oluşturulan ID'si
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: DayEntity): Long

    /**
     * Birden fazla gün ekler
     *
     * @param days Eklenecek günler listesi
     * @return Eklenen günlerin otomatik oluşturulan ID'leri
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days: List<DayEntity>): List<Long>

    /**
     * Bir günü günceller
     *
     * @param day Güncellenecek gün
     */
    @Update
    suspend fun updateDay(day: DayEntity)

    /**
     * Bir günü siler
     *
     * @param day Silinecek gün
     */
    @Delete
    suspend fun deleteDay(day: DayEntity)

    /**
     * Belirli bir ID'ye sahip günü veritabanından alır
     *
     * @param id Alınacak günün ID'si
     * @return Belirtilen ID'ye sahip gün
     */
    @Query("SELECT * FROM days WHERE id = :id")
    suspend fun getDayById(id: Long): DayEntity?
}