package dev.jdtech.jellyfin.setup.presentation.addresses

import java.util.UUID

sealed interface ServerAddressToggleAction {
    data class OnAddressClick(val addressId: UUID) : ServerAddressToggleAction
}
