package dev.jdtech.jellyfin.presentation.film.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jdtech.jellyfin.core.R as CoreR
import dev.jdtech.jellyfin.core.presentation.dummy.dummyServerAddress
import dev.jdtech.jellyfin.models.ServerAddress
import dev.jdtech.jellyfin.presentation.theme.FindroidTheme
import dev.jdtech.jellyfin.setup.presentation.addresses.ServerAddressToggleAction
import dev.jdtech.jellyfin.setup.presentation.addresses.ServerAddressToggleState
import java.net.URI
import java.util.UUID

/**
 * Findroid BE: one-tap switch between the addresses of the current server. Shows nothing with
 * fewer than two addresses. With exactly two a tap swaps to the other one, with more a tap opens a
 * menu to pick from.
 */
@Composable
fun ServerAddressToggle(
    state: ServerAddressToggleState,
    enabled: Boolean,
    onAction: (ServerAddressToggleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.addresses.size < 2) return

    val current = state.addresses.firstOrNull { it.id == state.currentAddressId }
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        AssistChip(
            onClick = {
                if (state.addresses.size == 2) {
                    state.addresses
                        .firstOrNull { it.id != state.currentAddressId }
                        ?.let { onAction(ServerAddressToggleAction.OnAddressClick(it.id)) }
                } else {
                    menuExpanded = true
                }
            },
            label = {
                Text(
                    text = current?.displayHost() ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            enabled = enabled,
            leadingIcon = {
                Icon(
                    painter = painterResource(CoreR.drawable.ic_server),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            },
        )
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            state.addresses.forEach { address ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = address.displayHost(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = {
                        menuExpanded = false
                        onAction(ServerAddressToggleAction.OnAddressClick(address.id))
                    },
                )
            }
        }
    }
}

/** Host (and port when present) of the address, falling back to the raw text if it can't parse. */
private fun ServerAddress.displayHost(): String =
    runCatching {
            val uri = URI(address)
            val host = uri.host ?: return@runCatching null
            if (uri.port != -1) "$host:${uri.port}" else host
        }
        .getOrNull() ?: address

@Composable
@Preview
private fun ServerAddressTogglePreview() {
    FindroidTheme {
        ServerAddressToggle(
            state =
                ServerAddressToggleState(
                    addresses =
                        listOf(
                            dummyServerAddress,
                            dummyServerAddress.copy(
                                id = UUID.randomUUID(),
                                address = "https://media.example.com",
                            ),
                        ),
                    currentAddressId = dummyServerAddress.id,
                ),
            enabled = true,
            onAction = {},
        )
    }
}
