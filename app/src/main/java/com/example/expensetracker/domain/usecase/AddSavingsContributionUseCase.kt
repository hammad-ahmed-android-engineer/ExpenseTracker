package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.SavingsContribution
import com.example.expensetracker.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class AddSavingsContributionUseCase @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository
) {
    suspend operator fun invoke(goalId: Long, contribution: SavingsContribution) {
        savingsGoalRepository.addContribution(goalId, contribution)
    }
}

