package com.developerstring.jetco.components.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun JetCoDatePicker(
    selectedDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showCalendar by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var chosenDate by remember { mutableStateOf(selectedDate) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Selected Date Display
        Text(
            text = chosenDate.toString(),
            color = Color.White,
            modifier = Modifier
                .clickable { showCalendar = !showCalendar }
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        if (showCalendar) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Header with month name
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "<",
                            modifier = Modifier.clickable {
                                currentMonth = currentMonth.minusMonths(1)
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = ">",
                            modifier = Modifier.clickable {
                                currentMonth = currentMonth.plusMonths(1)
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Days of week
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        DayOfWeek.values().forEach {
                            Text(
                                text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dates Grid
                    val firstDayOfMonth = currentMonth.atDay(1)
                    val lastDayOfMonth = currentMonth.atEndOfMonth()
                    val days = (1..lastDayOfMonth.dayOfMonth).map { day ->
                        currentMonth.atDay(day)
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        userScrollEnabled = false,
                        modifier = Modifier.height(250.dp)
                    ) {
                        items(days) { date ->
                            val isSelected = date == chosenDate
                            val isToday = date == LocalDate.now()

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isToday -> Color.LightGray
                                            else -> Color.Transparent
                                        },
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        chosenDate = date
                                        onDateSelected(date)
                                        showCalendar = false
                                    }
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
