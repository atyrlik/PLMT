package me.atyrlik.plmtalexandre.repository.local

import androidx.room.TypeConverter
import com.plumelabs.lib.bluetooth.MeasurementType

class EntitiesConverters {
    @TypeConverter
    fun toMeasurementType(value: String) = enumValueOf<MeasurementType>(value)
    @TypeConverter
    fun formMeasurementType (value: MeasurementType) = value.name
}