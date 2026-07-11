# Charts Screen Requirements

## Purpose

Provide a dedicated Charts tab for exploring financial trends with a shared filter and two time-series charts: balance and category totals.

## Assumptions

- The existing Transactions, Reports, and Settings tabs remain unchanged. Charts is a fourth bottom-navigation tab placed after Reports and before Settings.
- A currency must be selected for charts because amounts in different currencies must never be summed.
- Chart buckets use calendar months and include zero-value gaps within the selected period.
- Balance means income minus expense per calendar-month bucket inside the selected period and selected currency, not an all-time running account balance.

## Functional Requirements

### R1 — Charts navigation

1. The bottom bar shall display tabs in this order: Transactions, Reports, Charts, Settings.
2. Selecting Charts shall show a dedicated charts screen and preserve the existing Decompose tab/back behavior.
3. The existing Reports tab shall continue to open `ReportAnnualTotalComponent` without behavioral changes.
4. The charts screen shall own one filter state and pass the same filter to every chart.

### R2 — Currency filter

1. `FiltersSettings` shall support `Currency` independently of other filter controls.
2. When enabled, the filter UI shall show the selected currency and allow opening the existing currency selector.
3. By default, Charts shall select the currency having the greatest number of transactions within the selected period and other active non-currency filters.
4. Ties shall be resolved deterministically by currency code so the default does not flicker.
5. If that filter has no transactions, Charts shall fall back to the last-used currency and then `CurrencyEntity.RUB`.
6. An explicit user selection shall update `FilterEntity.currency` and remain selected until reset. The currency control shall expose a clear action that sets it to `null`; Reset shall also return a null currency to the owning screen.
7. Filters shall not resolve currency. When Apply returns null currency, Charts shall resolve it and shall not run a chart query until it has updated its authoritative filter with the result.
8. The readable/active-filter UI shall indicate when a currency constraint is applied.
9. Report database queries shall apply `currencyCode` when `FilterEntity.currency` is non-null.

### R3 — Balance line chart

1. The screen shall display a balance line chart for the selected period, selected currency, tags, category, and transaction type.
2. Each calendar-month point within the selected period shall equal filtered income minus filtered expense for the selected currency.
3. The component shall expose loading, content, empty, and failure states using the existing Component + internal UDF pattern.
4. Axes, markers, amount formatting, colors, and spacing shall use Vico and the design system.

### R4 — Category line chart

1. The screen shall display one time series per category for the same filter and monthly buckets.
2. Category colors and labels shall come from `CategoryEntity` and repository resources.
3. The chart shall provide a readable legend and marker values; it shall handle categories appearing or disappearing across buckets.
4. Loading, empty, and failure states shall be explicit.

### R5 — Refresh and consistency

1. Applying a filter shall refresh both charts from the same immutable filter snapshot.
2. Stale requests shall not overwrite results from a newer filter.
3. Screen and component state shall survive configuration changes using established Decompose/Essenty retention patterns.

## Quality Requirements

- Add unit tests for SQL mapping/use cases and chart reducers/mappers, including currency isolation, empty periods, negative balances, and category gaps.
- `./gradlew test` and the two-pass Detekt pre-commit workflow must pass.
- No schema migration is expected; the feature adds read queries only.

## Out of Scope

- Currency conversion or exchange rates.
- Daily/weekly bucket selection, chart exporting, comparisons, forecasting, or interactive category filtering from the chart.

## Acceptance

The feature is complete when a user can open Charts, choose a currency and other filters, apply them once, and see consistent balance and category monthly charts whose values match filtered transactions, while Reports continues to show the existing annual report.
