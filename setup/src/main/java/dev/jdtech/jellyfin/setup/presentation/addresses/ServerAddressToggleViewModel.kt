package dev.jdtech.jellyfin.setup.presentation.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jdtech.jellyfin.database.ServerDatabaseDao
import dev.jdtech.jellyfin.setup.domain.SetupRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

// Findroid BE: drives the address toggle on the home screen. It has its own event channel (rather
// than reusing ServersViewModel) because the server selection sheet also consumes ServersViewModel
// events, and a Channel hands each event to only one collector.
@HiltViewModel
class ServerAddressToggleViewModel
@Inject
constructor(private val repository: SetupRepository, private val database: ServerDatabaseDao) :
    ViewModel() {
    private val _state = MutableStateFlow(ServerAddressToggleState())
    val state = _state.asStateFlow()

    private val eventsChannel = Channel<ServerAddressToggleEvent>()
    val events = eventsChannel.receiveAsFlow()

    fun loadAddresses() {
        viewModelScope.launch { refresh() }
    }

    private suspend fun refresh() {
        try {
            val server = repository.getCurrentServer() ?: return
            val addresses = database.getServerWithAddresses(server.id).addresses
            _state.emit(
                ServerAddressToggleState(
                    addresses = addresses,
                    currentAddressId = server.currentServerAddressId,
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun setCurrentAddress(addressId: UUID) {
        viewModelScope.launch {
            // Same path as the server selection sheet: updates the database and the API base URL.
            repository.setCurrentAddress(addressId)
            refresh()
            eventsChannel.send(ServerAddressToggleEvent.AddressChanged)
        }
    }

    fun onAction(action: ServerAddressToggleAction) {
        when (action) {
            is ServerAddressToggleAction.OnAddressClick -> setCurrentAddress(action.addressId)
        }
    }
}
