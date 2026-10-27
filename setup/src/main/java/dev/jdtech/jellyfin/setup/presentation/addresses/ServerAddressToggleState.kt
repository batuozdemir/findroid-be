package dev.jdtech.jellyfin.setup.presentation.addresses

import dev.jdtech.jellyfin.models.ServerAddress
import java.util.UUID

data class ServerAddressToggleState(
    val addresses: List<ServerAddress> = emptyList(),
    val currentAddressId: UUID? = null,
)
