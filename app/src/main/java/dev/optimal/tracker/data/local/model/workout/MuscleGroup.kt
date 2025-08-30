package dev.optimal.tracker.data.local.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_group")
data class MuscleGroup(
    @PrimaryKey(autoGenerate = true) val muscleGroupId: Long = 0,
    val name: String
)