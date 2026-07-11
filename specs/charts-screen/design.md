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

Track whether currency selection is automatic or manual in internal filter state. On initialization and Reset, enter automatic mode and resolve the most-used currency for the current filter. When non-currency criteria change in automatic mode, resolve it again. An explicit currency choice or clear action enters manual mode and remains stable while other filters change. Mirror the category control by showing a trailing clear icon only for a selected currency. Charts requires a resolved currency before Apply/chart loading. If the selected filter contains no transactions, use `GetLastUsedCurrencyUseCase`; if that is also empty, use `CurrencyEntity.RUB`. Cover the complete fallback chain in reducer tests.

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

Add `ReportBalanceTimelineUseCase` and `ReportCategoryTimelineUseCase` to `feature:reports:api`, default implementations using `runSuspendCatching`, and factories in `buildReportsModule()`.

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
