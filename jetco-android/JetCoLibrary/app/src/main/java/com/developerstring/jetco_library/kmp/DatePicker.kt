package com.developerstring.jetco_library.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit // Import for the edit icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeParseException
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

fun LocalDate.daysInMonth(): Int {
    return when (monthNumber) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun LocalDate.plusMonths(months: Int): LocalDate {
    val newMonth = monthNumber + months
    var newYear = year
    var newMonthNumber = newMonth

    if (newMonthNumber > 12) {
        newYear += (newMonthNumber - 1) / 12
        newMonthNumber = (newMonthNumber - 1) % 12 + 1
    } else if (newMonthNumber < 1) {
        newYear += (newMonthNumber) / 12 - 1
        newMonthNumber = 12 + (newMonthNumber % 12)
        if (newMonthNumber == 0) newMonthNumber = 12
    }

    val days = LocalDate(newYear, newMonthNumber, 1).daysInMonth()
    val newDay = if (dayOfMonth > days) days else dayOfMonth

    return LocalDate(newYear, newMonthNumber, newDay)
}


@Composable
fun JetCoDatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentViewDate by remember { mutableStateOf(selectedDate) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }
    var isManualInputMode by remember { mutableStateOf(false) } // New state for manual input

    // State for manual text input
    var manualDateInput by remember { mutableStateOf(selectedDate.toString()) }
    var manualInputError by remember { mutableStateOf<String?>(null) }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val headerColor = MaterialTheme.colorScheme.primary
    val headerTextColor = MaterialTheme.colorScheme.onPrimary
    val daySelectedColor = MaterialTheme.colorScheme.secondary
    val daySelectedTextColor = Color.White
    val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        dismissButton = {},
        shape = RoundedCornerShape(12.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(headerColor)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                ) {
                    Text(
                        text = tempSelectedDate.dayOfWeek.name.take(3).capitalize(Locale.ROOT) + ", " +
                                tempSelectedDate.month.getDisplayName(JavaTextStyle.SHORT, Locale.getDefault()) + " " +
                                tempSelectedDate.dayOfMonth,
                        color = headerTextColor,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    // Edit Icon
                    IconButton(
                        onClick = {
                            isManualInputMode = !isManualInputMode
                            manualDateInput = tempSelectedDate.toString() // Initialize input with current selected date
                            manualInputError = null
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Date Manually",
                            tint = headerTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isManualInputMode) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = manualDateInput,
                            onValueChange = { newValue ->
                                manualDateInput = newValue
                                try {
                                    val parsedDate = LocalDate.parse(newValue)
                                    // Update tempSelectedDate immediately if valid for preview in header
                                    tempSelectedDate = parsedDate
                                    manualInputError = null
                                } catch (e: DateTimeParseException) {
                                    manualInputError = "Invalid date format (YYYY-MM-DD)"
                                } catch (e: IllegalArgumentException) {
                                    manualInputError = "Invalid date (e.g., Feb 30)"
                                }
                            },
                            label = { Text("Enter Date (YYYY-MM-DD)") },
                            isError = manualInputError != null,
                            supportingText = {
                                if (manualInputError != null) {
                                    Text(text = manualInputError!!, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { currentViewDate = currentViewDate.plusMonths(-1) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                        }
                        Text(
                            text = currentViewDate.month.getDisplayName(JavaTextStyle.FULL, Locale.getDefault()) + " " + currentViewDate.year,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { currentViewDate = currentViewDate.plusMonths(1) }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        daysOfWeek.forEach { day ->
                            Text(
                                text = day.take(3),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Calendar Grid
                    val firstDayOfMonth = LocalDate(currentViewDate.year, currentViewDate.monthNumber, 1)
                    val daysInMonth = firstDayOfMonth.daysInMonth()
                    val startDayOffset = firstDayOfMonth.dayOfWeek.isoDayNumber - 1

                    val paddedDays = List(startDayOffset) { "" } + (1..daysInMonth).map { it.toString() }
                    val rows = paddedDays.chunked(7)

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        rows.forEach { week ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                week.forEach { dayString ->
                                    val dayInt = dayString.toIntOrNull()
                                    val isSelected = dayInt != null &&
                                            currentViewDate.year == tempSelectedDate.year &&
                                            currentViewDate.monthNumber == tempSelectedDate.monthNumber &&
                                            dayInt == tempSelectedDate.dayOfMonth
                                    val isToday = dayInt != null &&
                                            currentViewDate.year == todayDate.year &&
                                            currentViewDate.monthNumber == todayDate.monthNumber &&
                                            dayInt == todayDate.dayOfMonth

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .let { m ->
                                                if (dayInt != null) {
                                                    m.clickable {
                                                        tempSelectedDate = LocalDate(currentViewDate.year, currentViewDate.monthNumber, dayInt)
                                                    }
                                                } else m
                                            }
                                            .background(
                                                if (isSelected) daySelectedColor else Color.Transparent,
                                                shape = RoundedCornerShape(50) // Circle shape
                                            )
                                            .let { m ->
                                                if (isToday && !isSelected) {
                                                    m.border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(50))
                                                } else m
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (dayInt != null) {
                                            Text(
                                                text = dayString,
                                                color = if (isSelected) daySelectedTextColor else MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("CANCEL") }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (isManualInputMode) {
                            try {
                                val parsedDate = LocalDate.parse(manualDateInput)
                                onDateSelected(parsedDate)
                                onDismiss()
                            } catch (e: DateTimeParseException) {
                                manualInputError = "Invalid date format (YYYY-MM-DD)"
                            } catch (e: IllegalArgumentException) {
                                manualInputError = "Invalid date (e.g., Feb 30)"
                            }
                        } else {
                            onDateSelected(tempSelectedDate)
                            onDismiss()
                        }
                    }) { Text("OK") }
                }
            }
        }
    )
}


@Composable
fun JetCoDatePickerDemo() {

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }


    var showPicker by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        selectedDate?.let {
            Text(text = "Selected Date: $it", style = MaterialTheme.typography.titleMedium)
        } ?: Text(text = "No Date Selected", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showPicker = true }) {
            Text("Open Date Picker")
        }

        if (showPicker) {
            JetCoDatePicker(
                selectedDate = selectedDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                onDateSelected = {
                    selectedDate = it
                    showPicker = false
                },
                onDismiss = { showPicker = false }

            )
        }
    }
}

// While Running App call JetCoDatePickerPreview Function In Main