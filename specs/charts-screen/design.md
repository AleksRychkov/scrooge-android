# Charts Screen Design

## Architecture

Follow the existing dependency direction:

```text
presentation:screen:charts
  -> presentation:component:filters
  -> presentation:component:balance-line-chart
  -> presentation:component:category-line-chart
      -> feature:reports:api
          -> core:database:api
              <- core:database:default (SQLDelight)
```

Register the new modules in `settings.gradle.kts`. Extend `presentation:screen:main:tabs` with a serialized `Charts` configuration and child that creates `ChartsComponent`. Keep the existing Reports configuration and `ReportAnnualTotalComponent` unchanged. Render bottom-navigation items in the order Transactions, Reports, Charts, Settings; Charts follows the same back behavior as the other non-default tabs.

## Filter Extension

`FilterEntity` already contains `currency: CurrencyEntity?`. In `presentation:component:filters`, add `FiltersSettings.Currency`, currency slot navigation, `CurrencyComponent`, currency events, reducer handling, UI control, modal, preview state, and resource strings. Preserve the existing currency package spelling when importing its public API.

Add a transaction-domain query/use case that returns the currency with the highest transaction count for a filter while ignoring `filter.currency`. Apply the period, tag, category, and transaction-type predicates and break equal counts by currency code. This belongs in `feature:transaction:api/default/di` and the transaction DAO/SQL rather than presentation code; the filters module already depends on `feature:transaction:api`.

Keep `FiltersComponent` presentation-only: it edits the supplied `FilterEntity`, preserves an explicitly selected currency while other criteria change, and returns `null` after clear or Reset. Mirror the category control by showing a trailing clear icon only for a selected currency. The owning screen is the source of truth for the applied filter.

When Charts receives a filter whose currency is null, dispatch a screen command that resolves the most-used currency for the non-currency filter. If the selected period has no transactions, use `GetLastUsedCurrencyUseCase`; if that is also empty, use `CurrencyEntity.RUB`. Cancel stale resolution commands, update the screen filter with the result, and only then propagate the same snapshot to both charts. Cover the fallback chain in Charts tests.

After currency resolution, resolve a null category to the category with the most matching transactions while ignoring `filter.category`. If no transaction matches, select a random available category constrained by `filter.transactionType` when present. Store both defaults in the authoritative Charts filter before loading either chart, so the category chart always renders one series.

The Charts filter modal exposes only year, currency, and category controls. Month, tag, and transaction-type controls remain available to other owning screens but are not shown on Charts.

## Screen Wireframe

```text
┌──────────────────────────────────────────┐
│ Charts              [2025 · RUB · filter]│
├──────────────────────────────────────────┤
│ ┌ Balance ─────────────────────────────┐ │
│ │             ╭──╮                    │ │
│ │    ╭──╮  ╭──╯  ╰──╮                 │ │
│ │ ───╯  ╰──╯        ╰──  Jan … Dec    │ │
│ └──────────────────────────────────────┘ │
│ ┌ Categories ──────────────────────────┐ │
│ │ Food ──  Transport ──  Other ──      │ │
│ │   ╭──╮       ╭────╮                  │ │
│ │ ──╯  ╰───────╯    ╰──  Jan … Dec    │ │
│ └──────────────────────────────────────┘ │
├──────────────────────────────────────────┤
│ Transactions  Reports  Charts  Settings │
└──────────────────────────────────────────┘
```

Each chart card independently renders one of four body states while retaining its title:

- Loading: centered existing progress indicator.
- Content: chart, axes, marker, and category legend where applicable.
- Empty: localized no-data text for the active filter.
- Failure: localized error text plus Retry action.

The screen scrolls vertically when both cards do not fit. The top filter action opens the shared filters modal; applying it refreshes both cards together.

## Data Layer

Add immutable time-series entities in `core:entity`, for example:

- `ReportBalanceTimelineEntity(points: ImmutableList<Point>)`
- `ReportCategoryTimelineEntity(series: ImmutableList<CategorySeries>)`
- Each point contains a `LocalDate` bucket and `Long` minor-unit amount.

Extend `ReportDao` with balance-timeline and category-timeline methods. Add SQLDelight queries grouped by year/month. Every report query, including existing totals, should accept `currencyCode` and apply `(:currencyCode IS NULL OR t.currencyCode = :currencyCode)`. Reuse the temporary tag-filter mechanism and existing category/type/date predicates. Mappers must sort buckets and synthesize missing month/category values as zero.

The balance timeline is the exception to the shared non-currency predicates: it uses only date range and currency. Category, tag, and transaction-type filters apply to the category timeline but must not change balance points.

Balance Total queries monthly income-minus-expense changes from the beginning of transaction history through the selected period end. Its mapper accumulates all earlier changes into an opening balance, emits the total at every selected month, carries totals through inactive months, and omits future months. It reuses the balance entity and bar renderer but has a dedicated report use case and retained component.

Add chart-specific report use cases to `feature:reports:api`, default implementations using `runSuspendCatching`, and factories in `buildReportsModule()`.

## Presentation Components

Each chart is a standalone Android library with public `Component`, `Content`, and `setFilters(FilterEntity)` APIs. Internally use retained UDF stores:

```text
External.SetFilter -> Reducer -> Load command
Load delegate -> use case -> Loaded / Empty / Failed event
Reducer -> Loading / Content / Empty / Failure state
```

Use request generations or `flatMapLatest` semantics so old loads cannot win. Map domain entities into immutable presentation state outside composables. Vico rendering belongs in `Content`; reuse styling concepts from `period-balance` but do not retain its stub data or `@file:Suppress("All")`.

`ChartsComponent` owns the current filter, filter modal, and both child chart components. `setFilter` updates screen state and both children synchronously. The screen uses a `Scaffold`, Charts title, `DsFilterAction`, and a vertically scrolling layout containing two design-system cards.

## Error and Empty Behavior

- Loading: show the existing progress treatment without stale values.
- Empty: show a localized no-data message while retaining filters.
- Failure: show a localized error and retry action using the current filter.
- A missing currency: resolve most-used, then last-used, then RUB before querying; never aggregate currencies.

## Verification

Test SQL/use-case results with multiple currencies and sparse months. Test reducers for rapid filter changes, success, empty, retry, and failure. Add Compose previews for all visual states. Run focused module tests, `./gradlew test`, `./gradlew assembleDebug`, and the repository Detekt workflow.
