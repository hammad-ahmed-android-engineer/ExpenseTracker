package com.example.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.usecase.GetUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val preferences: UserPreferences = UserPreferences(),
    val isReady: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = getUserPreferencesUseCase()
        .map { prefs ->
            MainUiState(
                preferences = prefs,
                isReady = true
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState()
        )
}

