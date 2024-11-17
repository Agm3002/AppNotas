package com.unison.appnotas.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.unison.appnotas.R

@Composable
fun SimpleDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Container
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.Black
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = dialogTitle,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 30.dp)
                )

                // Message
                Text(
                    text = dialogText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Dismiss button.
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.Dismiss),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    // Confirmation button
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.Confirm),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}