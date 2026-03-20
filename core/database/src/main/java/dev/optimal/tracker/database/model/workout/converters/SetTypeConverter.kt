package dev.optimal.tracker.database.model.workout.converters

import androidx.room.TypeConverter
import dev.optimal.tracker.model.workout.enums.SetType

class SetTypeConverter {
    @TypeConverter
    fun fromSetType(type: SetType): String {
        return type.name
    }

    @TypeConverter
    fun toSetType(value: String): SetType {
        return SetType.valueOf(value)
    }
}
