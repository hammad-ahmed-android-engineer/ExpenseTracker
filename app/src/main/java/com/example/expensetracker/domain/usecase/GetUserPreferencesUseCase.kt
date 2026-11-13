package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.UserPreferences
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> = repository.userPreferencesFlow
}
