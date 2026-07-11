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
- [x] Add automatic/manual currency state, currency events, reducer transitions, UI selection, reset behavior, and active-filter indication. (R2)
- [x] Add the most-used-currency transaction query/use case with period, tag, category, and type predicates plus deterministic tie-breaking. (R2)
- [x] Add reducer tests and Compose previews for automatic and manual currency filtering. (R2)

## 3. Add Report Domain and Persistence

- [x] Define balance/category timeline entities in `core:entity`. (R3, R4)
- [x] Add SQLDelight monthly balance and monthly-by-category queries with date, tag, category, type, and currency predicates. (R2–R4)
- [x] Apply the missing currency predicate to every existing SQL query backed by `FilterEntity`. (R2)
- [x] Extend `ReportDao`, `ReportMapper`, and `DefaultReportDao`; normalize sorting and missing buckets. (R3, R4)
- [x] Add report API use cases, default implementations, and DI registrations. (R3, R4)
- [x] Add data/use-case tests for currency counts/ties, multi-currency reports, sparse months, empty periods, income-only, and expense-only data. (R2–R4)

## 4. Build Chart Components

- [x] Register `presentation:component:balance-line-chart`; implement public API, retained UDF state, mapper, and Vico content. (R3, R5)
- [x] Add balance reducer/mapper tests and state previews. (R3)
- [ ] Register `presentation:component:category-line-chart`; implement public API, retained UDF state, legend, mapper, and multi-series Vico content. (R4, R5)
- [ ] Add category reducer/mapper tests for changing categories and zero-filled gaps. (R4)
- [ ] Verify both components discard stale filter results and support retry. (R5)

## 5. Build and Integrate Charts Screen

- [ ] Register `presentation:screen:charts` and implement `ChartsComponent`, internal interface/state, filter slot, and child contexts. (R1, R5)
- [ ] Wait for the filters component's automatically resolved currency, then propagate one filter snapshot to both charts. (R2, R5)
- [ ] Implement the Compose screen with title, filter action, two chart cards, and all UI states. (R1, R3, R4)
- [ ] Add a serialized Charts child to `presentation:screen:main:tabs`, preserve the existing Reports child, and update bottom-bar ordering and dependencies. (R1)
- [ ] Add navigation/screen component tests where practical. (R1, R5)

## 6. Validate

- [ ] Manually compare chart points with known seeded transactions across two currencies. (R2–R4)
- [ ] Check compact screens, long category names, dark theme, accessibility labels, and empty datasets. (R3, R4)
- [ ] Run focused tests, `./gradlew test`, and `./gradlew assembleDebug`.
- [ ] Run the two-pass `./gradlew detekt --auto-correct` workflow and inspect resulting changes.
