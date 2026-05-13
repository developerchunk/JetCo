/*
 * Copyright 2026 by Developer Chunk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developerstring.jetco_kmp.components.picker.model

import androidx.compose.runtime.Stable
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Determines the time display format.
 *
 * @property HOUR_12 12-hour clock with AM/PM indicator.
 * @property HOUR_24 24-hour clock.
 */
@Stable
enum class TimeFormat {
    HOUR_12,
    HOUR_24
}

/**
 * Determines how the picker is presented.
 *
 * @property DIALOG Picker is wrapped inside a Material 3 `AlertDialog`.
 * @property BOTTOM_SHEET Picker is shown inside a Material 3 `ModalBottomSheet`.
 * @property INLINE Picker content is rendered inline without a wrapping container.
 */
@Stable
enum class PickerDisplayMode {
    DIALOG,
    BOTTOM_SHEET,
    INLINE
}

/**
 * Determines the date format pattern for display.
 *
 * @property DAY_MONTH_YEAR d / MMM / yyyy — e.g. 15 / Jan / 2026
 * @property MONTH_DAY_YEAR MMM / d / yyyy — e.g. Jan / 15 / 2026
 * @property YEAR_MONTH_DAY yyyy / MMM / d — e.g. 2026 / Jan / 15
 */
@Stable
enum class DateOrder {
    DAY_MONTH_YEAR,
    MONTH_DAY_YEAR,
    YEAR_MONTH_DAY
}

/**
 * Represents a selected date from the picker.
 *
 * @property year Selected year (e.g. 2026).
 * @property month Selected month (1–12).
 * @property dayOfMonth Selected day of the month (1–31).
 */
@Stable
data class PickerDate(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
) {
    /**
     * Formats the date as `yyyy-MM-dd`.
     */
    fun toFormattedString(): String {
        return "${year}-${month.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
    }

    /**
     * Formats the date using the given [pattern].
     *
     * Supported tokens: `yyyy`, `MM`, `dd`, `MMM`, `MMMM`, `d`, `M`.
     */
    fun format(pattern: String): String {
        val monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val shortMonthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        // Use NUL-delimited placeholders to avoid conflicts between
        // token replacement and substituted text (e.g. "March" containing 'M').
        val phYear = "\u0000Y\u0000"
        val phFullMonth = "\u0000FM\u0000"
        val phShortMonth = "\u0000SM\u0000"
        val phMonth2 = "\u0000M2\u0000"
        val phDay2 = "\u0000D2\u0000"
        val phDay = "\u0000D\u0000"
        val phMonth = "\u0000M\u0000"

        return pattern
            // First pass: replace tokens with placeholders (longest first)
            .replace("yyyy", phYear)
            .replace("MMMM", phFullMonth)
            .replace("MMM", phShortMonth)
            .replace("MM", phMonth2)
            .replace("dd", phDay2)
            .replace("d", phDay)
            .replace("M", phMonth)
            // Second pass: replace placeholders with actual values
            .replace(phYear, year.toString())
            .replace(phFullMonth, monthNames.getOrElse(month - 1) { "" })
            .replace(phShortMonth, shortMonthNames.getOrElse(month - 1) { "" })
            .replace(phMonth2, month.toString().padStart(2, '0'))
            .replace(phDay2, dayOfMonth.toString().padStart(2, '0'))
            .replace(phDay, dayOfMonth.toString())
            .replace(phMonth, month.toString())
    }

    companion object {
        /**
         * Returns the current date using kotlinx-datetime.
         */
        fun now(): PickerDate {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            return PickerDate(
                year = today.year,
                month = today.monthNumber,
                dayOfMonth = today.dayOfMonth
            )
        }
    }
}

/**
 * Represents a selected time from the picker.
 *
 * @property hour Hour value (0–23).
 * @property minute Minute value (0–59).
 * @property second Second value (0–59).
 */
@Stable
data class PickerTime(
    val hour: Int,
    val minute: Int,
    val second: Int = 0
) {
    /**
     * Formats the time as `HH:mm` (24-hour) or `hh:mm a` (12-hour).
     */
    fun toFormattedString(format: TimeFormat = TimeFormat.HOUR_24): String {
        return when (format) {
            TimeFormat.HOUR_24 -> {
                "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
            }
            TimeFormat.HOUR_12 -> {
                val amPm = if (hour < 12) "AM" else "PM"
                val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                "${h.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
            }
        }
    }

    companion object {
        /**
         * Returns the current time using kotlinx-datetime.
         */
        fun now(): PickerTime {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return PickerTime(
                hour = now.hour,
                minute = now.minute,
                second = now.second
            )
        }
    }
}

/**
 * Represents a selected date and time from the picker.
 *
 * @property date The date component.
 * @property time The time component.
 */
@Stable
data class PickerDateTime(
    val date: PickerDate,
    val time: PickerTime
) {
    companion object {
        /**
         * Returns the current date and time.
         */
        fun now(): PickerDateTime {
            return PickerDateTime(
                date = PickerDate.now(),
                time = PickerTime.now()
            )
        }
    }
}

/**
 * Represents a date range selection with a start and end date.
 *
 * @property startDate The starting date of the range.
 * @property endDate The ending date of the range.
 */
@Stable
data class PickerDateRange(
    val startDate: PickerDate,
    val endDate: PickerDate
)
