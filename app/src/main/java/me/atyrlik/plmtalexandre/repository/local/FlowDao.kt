package me.atyrlik.plmtalexandre.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.atyrlik.plmtalexandre.entities.FlowMeasure

@Dao
interface FlowDao {
    @Query("SELECT * FROM flowmeasure ORDER BY timestamp ")
    fun getAll(): Flow<List<FlowMeasure>>

    @Insert
    suspend fun insert(measure: FlowMeasure)
}