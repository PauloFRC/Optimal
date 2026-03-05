package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverter

enum class MuscleGroupRole {
    PRIMARY,
    SECONDARY
}

class Converters {
    @TypeConverter
    fun fromRole(role: MuscleGroupRole): String = role.name

    @TypeConverter
    fun toRole(value: String): MuscleGroupRole = MuscleGroupRole.valueOf(value)
}

@Entity(
    primaryKeys = ["exerciseId", "muscleGroupId"],
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MuscleGroup::class,
            parentColumns = ["muscleGroupId"],
            childColumns = ["muscleGroupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId"), Index("muscleGroupId")]
)
data class ExerciseMuscleGroupCrossRef(
    val exerciseId: Long,
    val muscleGroupId: Long,
    val role: MuscleGroupRole
)

data class MuscleGroupWithRole(
    @Embedded val muscleGroup: MuscleGroup,
    val role: MuscleGroupRole
)
