package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
internal fun DropDownPreferenceWidget(
    preference: Preference.PreferenceItem.DropDownMenuPreference,
    value: String,
    onValueChange: (String) -> Unit
) {
    val (isExpanded, expand) = remember { mutableStateOf(false) }

    TextPreferenceWidgetRes(
        preference = preference,
        summary = preference.entries[value],
        onClick = { expand(!isExpanded) }
    )

    Box(
        modifier = Modifier.padding(start = 64.dp)
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { expand(!isExpanded) }
        ) {
            preference.entries.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(item.key)
                        expand(!isExpanded)
                    },
                    text = {
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.bodySmall.merge()
                        )
                    }
                )
            }
        }
    }
}