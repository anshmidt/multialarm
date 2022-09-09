package com.anshmidt.multialarm.view.helpers

class NumberPickerHelper {

    fun getNumberPickerData(numbers: List<Int>): NumberPickerData {
        val firstIndex = 0
        val lastIndex = numbers.size - 1
        val displayedValues = numbers.map { it.toString() }.toTypedArray()

        return NumberPickerData(
            minValue = firstIndex,
            maxValue = lastIndex,
            displayedValues = displayedValues
        )
    }

}