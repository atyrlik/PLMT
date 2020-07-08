package me.atyrlik.plmtalexandre.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.atyrlik.plmtalexandre.entities.FlowMeasure

@Database(entities = arrayOf(FlowMeasure::class), version = 1)
@TypeConverters(EntitiesConverters::class)
abstract class MeasuresDatabase : RoomDatabase() {
    abstract fun flowDao(): FlowDao
}
