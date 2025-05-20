package com.aliaktas.taskme.data.repository

import com.aliaktas.taskme.data.local.DayDao
import com.aliaktas.taskme.data.local.DayEntity
import com.aliaktas.taskme.domain.model.Day // Domain modelini import ettiğinden emin ol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gün verilerini yöneten repository sınıfı
 * ViewModel ile veritabanı arasında köprü görevi görür
 *
 * @param dayDao Gün veritabanı erişim nesnesi
 */
@Singleton
class DayRepository @Inject constructor(
    private val dayDao: DayDao
) {
    /**
     * Tüm günleri veritabanından alır ve domain modeline dönüştürür
     *
     * @return Günlerin domain model listesi (Flow tipinde)
     */
    fun getAllDays(): Flow<List<Day>> {
        return dayDao.getAllDays().map { dayEntities ->
            dayEntities.map { it.toDomainModel() }
        }
    }

    /**
     * Yeni bir gün ekler
     *
     * @param day Eklenecek gün (domain model)
     * @return Eklenen günün ID'si
     */
    suspend fun insertDay(day: Day): Long { // Parametre: Day (domain model)
        return dayDao.insertDay(day.toEntity()) // Domain'den Entity'e çevrilip DAO'ya veriliyor
    }

    /**
     * Birden fazla gün ekler
     * ViewModel'den domain modeli listesi (List<Day>) alacak şekilde güncellendi.
     *
     * @param days Eklenecek günler listesi (domain model)
     * @return Eklenen günlerin ID'leri
     */
    suspend fun insertDays(days: List<Day>): List<Long> { // **** DÜZELTİLDİ: Parametre List<Day> oldu ****
        // Domain model listesini entity model listesine çeviriyoruz
        val dayEntities = days.map { domainDay -> // Her bir domainDay için toEntity çağır
            domainDay.toEntity() // **** DÜZELTİLDİ: domainDay.toEntity() olmalı ****
        }
        return dayDao.insertDays(dayEntities) // DAO, List<DayEntity> bekliyor olmalı
    }

    /**
     * Bir günü günceller
     *
     * @param day Güncellenecek gün (domain model)
     */
    suspend fun updateDay(day: Day) { // Parametre: Day (domain model)
        dayDao.updateDay(day.toEntity()) // Domain'den Entity'e çevrilip DAO'ya veriliyor
    }

    /**
     * Bir günü siler
     *
     * @param day Silinecek gün (domain model)
     */
    suspend fun deleteDay(day: Day) { // Parametre: Day (domain model)
        dayDao.deleteDay(day.toEntity()) // Domain'den Entity'e çevrilip DAO'ya veriliyor
    }

    /**
     * Belirli bir ID'ye sahip günü alır
     *
     * @param id Alınacak günün ID'si
     * @return İstenen gün (domain model) veya null
     */
    suspend fun getDayById(id: Long): Day? {
        return dayDao.getDayById(id)?.toDomainModel()
    }

    /**
     * Day domain modelini DayEntity veritabanı modeline dönüştürür
     */
    private fun Day.toEntity(): DayEntity { // Alıcı: Day (com.aliaktas.taskme.domain.model.Day)
        return DayEntity(
            id = this.id,
            name = this.name,
            order = this.order
        )
    }

    /**
     * DayEntity veritabanı modelini Day domain modeline dönüştürür
     */
    private fun DayEntity.toDomainModel(): Day { // Alıcı: DayEntity
        return Day(
            id = this.id,
            name = this.name,
            order = this.order
        )
    }
}