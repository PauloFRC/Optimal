package dev.optimal.tracker.utils

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object OptimalDateTimeFormatter {
    fun formatDuration(startDate: LocalDateTime, endDate: LocalDateTime): String {
        val duration = Duration.between(startDate, endDate)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return String.format(Locale.US, "%dh %02dm", hours, minutes)
    }

    fun formatDate(date: LocalDateTime, showCurrentYear: Boolean = false): String {
        val isCurrentYear = date.year == LocalDate.now().year
        val pattern = if (!showCurrentYear && isCurrentYear) "MMMM d" else "MMMM d, yyyy"
        return date.toLocalDate().format(DateTimeFormatter.ofPattern(pattern))
    }
}
