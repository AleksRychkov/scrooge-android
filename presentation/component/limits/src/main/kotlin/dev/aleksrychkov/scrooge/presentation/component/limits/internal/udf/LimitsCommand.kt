package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

internal sealed interface LimitsCommand {
    data object ObserveLimits : LimitsCommand
    data object ObserveTotals : LimitsCommand
}