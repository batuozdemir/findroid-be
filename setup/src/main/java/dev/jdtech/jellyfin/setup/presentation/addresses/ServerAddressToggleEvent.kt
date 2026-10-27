package dev.jdtech.jellyfin.setup.presentation.addresses

sealed interface ServerAddressToggleEvent {
    data object AddressChanged : ServerAddressToggleEvent
}
