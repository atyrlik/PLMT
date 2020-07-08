package me.atyrlik.plmtalexandre.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plumelabs.lib.bluetooth.MeasurementType

@Entity
data class FlowMeasure(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "aqi") val aqi: Float?,
    @ColumnInfo(name = "timestamp") val timestamp: Long?,
    @ColumnInfo(name = "measurement_type") val type: MeasurementType?
)