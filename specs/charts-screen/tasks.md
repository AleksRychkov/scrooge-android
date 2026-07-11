# Charts Screen Implementation Tasks

Tasks are ordered by dependency. Complete each checkbox with focused tests before moving upward.

## 1. Confirm Product Semantics

- [x] Confirm navigation: retain Reports and add Charts as the fourth tab, after Reports and before Settings. (R1)
- [x] Confirm calendar-month buckets and per-month balance (`income - expense`) for the selected period and currency, rather than cumulative balance. (R3)
- [x] Confirm currency fallback: most-used for the selected filter, then last-used, then RUB. (R2)
- [x] Produce a screen/chart wireframe covering loading, empty, failure, and content states. (R3, R4)

## 2. Extend Filters

- [x] Add `FiltersSettings.Currency` and localized labels. (R2)
- [x] Add currency modal navigation and reuse `CurrencyComponent`/`CurrencyModal`. (R2)
- [x] Keep currency editing presentation-only: explicit selection/clear, reset to null, and active-filter indication. (R2)
- [x] Add the most-used-currency transaction query/use case with period, tag, category, and type predicates plus deterministic tie-breaking. (R2)
- [x] Add most-used-category resolution with a transaction-type-aware random category fallback. (R4)
- [x] Add reducer tests and Compose previews for selected and cleared currency filtering. (R2)

## 3. Add Report Domain and Persistence

- [x] Define balance/category timeline entities in `core:entity`. (R3, R4)
- [x] Add SQLDelight monthly balance and monthly-by-category queries with date, tag, category, type, and currency predicates. (R2–R4)
- [x] Apply the missing currency predicate to every existing SQL query backed by `FilterEntity`. (R2)
- [x] Extend `ReportDao`, `ReportMapper`, and `DefaultReportDao`; normalize sorting and missing buckets. (R3, R4)
- [x] Add report API use cases, default implementations, and DI registrations. (R3, R4)
- [x] Add all-time Balance Total persistence, mapper, use case, and opening-balance tests. (R4a)
- [x] Add data/use-case tests for currency counts/ties, multi-currency reports, sparse months, empty periods, income-only, and expense-only data. (R2–R4)

## 4. Build Chart Components

- [x] Register `presentation:component:balance-line-chart`; implement public API, retained UDF state, mapper, and Vico content. (R3, R5)
- [x] Add balance reducer/mapper tests and state previews. (R3)
- [x] Register `presentation:component:category-line-chart`; implement public API, retained UDF state, category label, mapper, and Vico content. (R4, R5)
- [x] Add category reducer/mapper tests for changing categories and zero-filled gaps. (R4)
- [x] Verify both components discard stale filter results and support retry. (R5)
- [x] Add a retained Balance Total chart using the shared balance bar rendering and interaction behavior. (R4a, R5)

## 5. Build and Integrate Charts Screen

- [x] Register `presentation:screen:charts` and implement `ChartsComponent`, internal interface/state, filter slot, and child contexts. (R1, R5)
- [x] Resolve a null currency in Charts, then propagate one resolved filter snapshot to the filter state and both charts. (R2, R5)
- [x] Implement the Compose screen with title, filter action, chart cards, and all UI states. (R1, R3, R4)
- [x] Add the Balance Total card and propagate the resolved filter to it with the other charts. (R4a, R5)
- [x] Add a serialized Charts child to `presentation:screen:main:tabs`, preserve the existing Reports child, and update bottom-bar ordering and dependencies. (R1)

## 6. Validate

- [ ] Manually compare chart points with known seeded transactions across two currencies. (R2–R4)
- [ ] Check compact screens, long category names, dark theme, accessibility labels, and empty datasets. (R3, R4)
- [ ] Run focused tests, `./gradlew test`, and `./gradlew assembleDebug`.
- [ ] Run the two-pass `./gradlew detekt --auto-correct` workflow and inspect resulting changes.
