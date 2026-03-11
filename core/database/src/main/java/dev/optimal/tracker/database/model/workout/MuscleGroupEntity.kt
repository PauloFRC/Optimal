package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MuscleGroupEntity(
    @PrimaryKey(autoGenerate = true) val muscleGroupId: Long = 0,
    val name: String
)
