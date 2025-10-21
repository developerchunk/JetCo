package com.developerstring.jetco_library
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@Preview
@Composable
fun JetCoDatePickerPreview() {
    JetCoDatePicker(
        selectedDate = LocalDate.now(),
        onDateSelected = { println("Selected: $it") }
    )
}