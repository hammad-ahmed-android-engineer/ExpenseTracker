package com.example.expensetracker.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

fun String.toCategoryIconVector(): ImageVector = when (this) {
    "Restaurant" -> Icons.Default.Restaurant
    "ShoppingCart" -> Icons.Default.ShoppingCart
    "DirectionsCar" -> Icons.Default.DirectionsCar
    "ShoppingBag" -> Icons.Default.ShoppingBag
    "Movie" -> Icons.Default.Movie
    "LocalHospital" -> Icons.Default.LocalHospital
    "School" -> Icons.Default.School
    "Home" -> Icons.Default.Home
    "FlashOn" -> Icons.Default.FlashOn
    "Flight" -> Icons.Default.Flight
    "Subscriptions" -> Icons.Default.Subscriptions
    "SelfImprovement" -> Icons.Default.SelfImprovement
    "Security" -> Icons.Default.Security
    "CardGiftcard" -> Icons.Default.CardGiftcard
    "ReceiptLong" -> Icons.Default.ReceiptLong
    "Payments" -> Icons.Default.Payments
    "Work" -> Icons.Default.Work
    "Store" -> Icons.Default.Store
    "TrendingUp" -> Icons.Default.TrendingUp
    "Apartment" -> Icons.Default.Apartment
    "Redeem" -> Icons.Default.Redeem
    "AccountBalance" -> Icons.Default.AccountBalance
    "AccountBalanceWallet" -> Icons.Default.AccountBalanceWallet
    "CreditCard" -> Icons.Default.CreditCard
    "Savings" -> Icons.Default.Savings
    "MoreHoriz" -> Icons.Default.MoreHoriz
    else -> Icons.Default.Category
}

