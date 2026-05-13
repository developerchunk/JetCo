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

package com.developerstring.jetco_kmp.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_kmp.components.picker.components.PickerContainer
import com.developerstring.jetco_kmp.components.picker.components.PickerHeader
import com.developerstring.jetco_kmp.components.picker.components.PickerWheel
import com.developerstring.jetco_kmp.components.picker.config.DatePickerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerContainerConfig
import com.developerstring.jetco_kmp.components.picker.config.PickerDefaults
import com.developerstring.jetco_kmp.components.picker.config.PickerHeaderConfig
import com.developerstring.jetco_kmp.components.picker.config.WheelPickerConfig
import com.developerstring.jetco_kmp.components.picker.model.PickerDisplayMode
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// ━━━━━━━━━━━━━━━━━━━━━  Private Helpers  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

private val FULL_MONTHS = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

private val SHORT_MONTHS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private fun buildMonthItems(config: DatePickerConfig): List<String> {
    return config.customMonthNames
        ?: if (config.showMonthAsNumber) {
            (1..12).map { it.toString().padStart(2, '0') }
        } else if (config.showShortMonths) {
            SHORT_MONTHS
        } else {
            FULL_MONTHS
        }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelMonthYearPicker (inline)  ━━━━━━━━━━━━━━

/**
 * An inline wheel-based month & year picker.
 *
 * Only displays month and year columns — the day component is omitted.
 * Useful for credit card expiry, billing period, or report filters.
 *
 * ### Example
 * ```kotlin
 * WheelMonthYearPicker(
 *     startMonth = 6,
 *     startYear = 2026,
 *     onDoneClick = { month, year -> /* use values */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startMonth Initially selected month (1–12).
 * @param startYear Initially selected year.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration (month names, years range).
 * @param onDoneClick Callback when "Done" is pressed.
 * @param onSelectionChange Callback when month or year changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelMonthYearPicker(
    modifier: Modifier = Modifier,
    startMonth: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
    startYear: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year,
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Month & Year"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    onDoneClick: (month: Int, year: Int) -> Unit = { _, _ -> },
    onSelectionChange: (month: Int, year: Int) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {}
) {
    val years = (dateConfig.yearsRange.first..dateConfig.yearsRange.last).toList()
    val yearItems = years.map { it.toString() }
    val monthItems = buildMonthItems(dateConfig)

    var selectedMonth by remember { mutableIntStateOf(startMonth) }
    var selectedYear by remember { mutableIntStateOf(startYear) }

    val yearInitialIndex = years.indexOf(selectedYear).coerceAtLeast(0)
    val monthInitialIndex = (selectedMonth - 1).coerceIn(0, 11)

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(selectedMonth, selectedYear) },
            onCancel = onCancel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PickerWheel(
                items = monthItems,
                initialIndex = monthInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1.2f)
            ) { index ->
                selectedMonth = index + 1
                onSelectionChange(selectedMonth, selectedYear)
            }

            PickerWheel(
                items = yearItems,
                initialIndex = yearInitialIndex,
                config = wheelConfig,
                modifier = Modifier.weight(1f)
            ) { index ->
                selectedYear = years[index]
                onSelectionChange(selectedMonth, selectedYear)
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelMonthYearPickerView (with container)  ━━

/**
 * A month & year picker displayed inside a dialog, bottom sheet, or inline.
 *
 * ### Example — Dialog
 * ```kotlin
 * WheelMonthYearPickerView(
 *     visible = showPicker,
 *     displayMode = PickerDisplayMode.DIALOG,
 *     onDoneClick = { month, year -> /* use values */ },
 *     onDismiss = { showPicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startMonth Initially selected month (1–12).
 * @param startYear Initially selected year.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected month and year.
 * @param onSelectionChange Callback on selection change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelMonthYearPickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startMonth: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
    startYear: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year,
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Month & Year"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (month: Int, year: Int) -> Unit = { _, _ -> },
    onSelectionChange: (month: Int, year: Int) -> Unit = { _, _ -> },
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelMonthYearPicker(
            modifier = modifier,
            startMonth = startMonth,
            startYear = startYear,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            dateConfig = dateConfig,
            onDoneClick = { month, year ->
                onDoneClick(month, year)
                onDismiss()
            },
            onSelectionChange = onSelectionChange,
            onCancel = onDismiss
        )
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelYearPicker (inline)  ━━━━━━━━━━━━━━━━━━━

/**
 * An inline wheel-based year-only picker.
 *
 * Displays a single year column. Useful for fiscal year selection,
 * birth year, or graduation year.
 *
 * ### Example
 * ```kotlin
 * WheelYearPicker(
 *     startYear = 2026,
 *     yearsRange = IntRange(2000, 2050),
 *     onDoneClick = { year -> /* use year */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startYear Initially selected year.
 * @param yearsRange Allowed year range.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param onDoneClick Callback when "Done" is pressed.
 * @param onYearChange Callback when the year changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelYearPicker(
    modifier: Modifier = Modifier,
    startYear: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year,
    yearsRange: IntRange = IntRange(1922, 2122),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Year"),
    onDoneClick: (year: Int) -> Unit = {},
    onYearChange: (year: Int) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val years = (yearsRange.first..yearsRange.last).toList()
    val yearItems = years.map { it.toString() }

    var selectedYear by remember { mutableIntStateOf(startYear) }
    val yearInitialIndex = years.indexOf(selectedYear).coerceAtLeast(0)

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(selectedYear) },
            onCancel = onCancel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PickerWheel(
                items = yearItems,
                initialIndex = yearInitialIndex,
                config = wheelConfig,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                selectedYear = years[index]
                onYearChange(selectedYear)
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelYearPickerView (with container)  ━━━━━━━

/**
 * A year-only picker displayed inside a dialog, bottom sheet, or inline.
 *
 * ### Example — Bottom Sheet
 * ```kotlin
 * WheelYearPickerView(
 *     visible = showYearPicker,
 *     displayMode = PickerDisplayMode.BOTTOM_SHEET,
 *     startYear = 2026,
 *     onDoneClick = { year -> /* use year */ },
 *     onDismiss = { showYearPicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startYear Initially selected year.
 * @param yearsRange Allowed year range.
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected year.
 * @param onYearChange Callback on year change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelYearPickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startYear: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year,
    yearsRange: IntRange = IntRange(1922, 2122),
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Year"),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (year: Int) -> Unit = {},
    onYearChange: (year: Int) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelYearPicker(
            modifier = modifier,
            startYear = startYear,
            yearsRange = yearsRange,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            onDoneClick = { year ->
                onDoneClick(year)
                onDismiss()
            },
            onYearChange = onYearChange,
            onCancel = onDismiss
        )
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelMonthPicker (inline)  ━━━━━━━━━━━━━━━━━━

/**
 * An inline wheel-based month-only picker.
 *
 * Displays a single month column. Useful for subscription period,
 * birthday month, or report month selection.
 *
 * ### Example
 * ```kotlin
 * WheelMonthPicker(
 *     startMonth = 3,
 *     showShortMonths = true,
 *     onDoneClick = { month -> /* use month (1-12) */ },
 * )
 * ```
 *
 * @param modifier Modifier applied to the outer container.
 * @param startMonth Initially selected month (1–12).
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration (for month display names).
 * @param onDoneClick Callback when "Done" is pressed.
 * @param onMonthChange Callback when the month changes.
 * @param onCancel Callback when "Cancel" is pressed.
 */
@Composable
fun WheelMonthPicker(
    modifier: Modifier = Modifier,
    startMonth: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Month"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    onDoneClick: (month: Int) -> Unit = {},
    onMonthChange: (month: Int) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val monthItems = buildMonthItems(dateConfig)

    var selectedMonth by remember { mutableIntStateOf(startMonth) }
    val monthInitialIndex = (selectedMonth - 1).coerceIn(0, 11)

    Column(modifier = modifier.fillMaxWidth()) {
        PickerHeader(
            config = headerConfig,
            onDone = { onDoneClick(selectedMonth) },
            onCancel = onCancel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PickerWheel(
                items = monthItems,
                initialIndex = monthInitialIndex,
                config = wheelConfig,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                selectedMonth = index + 1
                onMonthChange(selectedMonth)
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━  WheelMonthPickerView (with container)  ━━━━━━

/**
 * A month-only picker displayed inside a dialog, bottom sheet, or inline.
 *
 * ### Example — Dialog
 * ```kotlin
 * WheelMonthPickerView(
 *     visible = showMonthPicker,
 *     displayMode = PickerDisplayMode.DIALOG,
 *     dateConfig = PickerDefaults.datePickerConfig(showShortMonths = true),
 *     onDoneClick = { month -> /* 1-12 */ },
 *     onDismiss = { showMonthPicker = false },
 * )
 * ```
 *
 * @param modifier Modifier applied to the picker content.
 * @param visible Whether the picker is shown.
 * @param displayMode How the picker is presented.
 * @param startMonth Initially selected month (1–12).
 * @param wheelConfig Wheel scrolling configuration.
 * @param headerConfig Header configuration.
 * @param dateConfig Date-specific configuration.
 * @param containerConfig Container visual configuration.
 * @param dragHandle Custom drag handle for bottom sheet.
 * @param onDoneClick Callback with selected month (1–12).
 * @param onMonthChange Callback on month change.
 * @param onDismiss Callback on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelMonthPickerView(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    displayMode: PickerDisplayMode = PickerDisplayMode.BOTTOM_SHEET,
    startMonth: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber,
    wheelConfig: WheelPickerConfig = PickerDefaults.wheelPickerConfig(),
    headerConfig: PickerHeaderConfig = PickerDefaults.headerConfig(title = "Select Month"),
    dateConfig: DatePickerConfig = PickerDefaults.datePickerConfig(),
    containerConfig: PickerContainerConfig = PickerDefaults.containerConfig(),
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDoneClick: (month: Int) -> Unit = {},
    onMonthChange: (month: Int) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    PickerContainer(
        visible = visible,
        displayMode = displayMode,
        containerConfig = containerConfig,
        dragHandle = dragHandle,
        onDismiss = onDismiss
    ) {
        WheelMonthPicker(
            modifier = modifier,
            startMonth = startMonth,
            wheelConfig = wheelConfig,
            headerConfig = headerConfig,
            dateConfig = dateConfig,
            onDoneClick = { month ->
                onDoneClick(month)
                onDismiss()
            },
            onMonthChange = onMonthChange,
            onCancel = onDismiss
        )
    }
}
