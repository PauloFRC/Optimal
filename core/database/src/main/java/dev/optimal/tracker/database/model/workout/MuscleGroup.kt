package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_group")
data class MuscleGroup(
    @PrimaryKey(autoGenerate = true) val muscleGroupId: Long = 0,
    val name: String
)
