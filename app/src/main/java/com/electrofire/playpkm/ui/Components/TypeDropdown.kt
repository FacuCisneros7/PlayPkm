package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.electrofire.playpkm.Data.PokemonTypeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    label: String,
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    showEmpty: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val types = if (showEmpty) PokemonTypeUtils.typesWithEmpty else PokemonTypeUtils.types

    ExposedDropdownMenuBox(
        expanded = if (enabled) expanded else false,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier.width(200.dp)
    ) {
        OutlinedTextField(
            value = selectedType ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label, style = MaterialTheme.typography.bodyLarge) },
            trailingIcon = { if (enabled) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                disabledLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.tertiary.copy(0.9f)
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = type.typeRes),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = type.spanishName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onClick = {
                        onTypeSelected(type.spanishName)
                        expanded = false
                    }
                )
            }
        }
    }
}
