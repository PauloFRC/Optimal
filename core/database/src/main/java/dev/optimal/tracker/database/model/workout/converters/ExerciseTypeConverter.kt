package dev.optimal.tracker.database.model.workout.converters

import androidx.room.TypeConverter
import dev.optimal.tracker.database.enums.ExerciseType

class ExerciseTypeConverter {
    @TypeConverter
    fun fromExerciseType(type: ExerciseType): String {
        return type.name
    }

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType {
        return ExerciseType.valueOf(value)
    }
}
