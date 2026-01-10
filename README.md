<p align='center'>
 <a href='https://github.com/sponsors/alexandresanlim'>
  <img src='https://raw.githubusercontent.com/AleksRychkov/scrooge-android/refs/heads/main/assets/icon.svg?token=GHSAT0AAAAAADS6CNTSIGZLOMWZKXI6L6RQ2LCTXAQ' width=150 />
 </a>
</p>


# Scrooge 🐷💰

**Scrooge** is an Android application designed to help you **track your incomes, expenses, and
financial flow** with clarity and efficiency.

---

## Architecture 🏗️

Modules are divided into:

- **Core** — shared resources, database, entities, utilities, and design system.
- **Feature** — domain-specific functionality (category, currency, transaction, reports, tags,
  themes, etc.).
- **Presentation** — screens and reusable UI components.

```mermaid
graph TD

%% ===== LAYERS / COLUMNS =====
    subgraph APP_LAYER["App"]
        APP["app"]
    end

    subgraph PRES_LAYER["Presentation"]
        PRES_SCREEN["presentation:screen"]
        PRES_COMPONENT["presentation:component"]
    end

    subgraph FEATURE_LAYER_API["Feature: API"]
        FEATURE_API["feature:api"]
    end

    subgraph FEATURE_LAYER_DEFAULT["Feature: Default"]
        FEATURE_DEFAULT["feature:default"]
    end

    subgraph FEATURE_LAYER_DI["Feature: DI"]
        FEATURE_DI["feature:di"]
    end

    subgraph CORE_LAYER["Core"]
        CORE["core"]
    end

%% ===== HIGH-LEVEL EDGES =====

%% App dependencies
    APP --> FEATURE_DI
    APP --> PRES_SCREEN

%% Presentation depends on API
    PRES_SCREEN --> FEATURE_API
    PRES_SCREEN --> CORE
    PRES_SCREEN --> PRES_COMPONENT

    PRES_COMPONENT --> FEATURE_API
    PRES_COMPONENT --> CORE

%% Feature dependencies
    FEATURE_DEFAULT --> FEATURE_API
    FEATURE_DEFAULT --> CORE

    FEATURE_DI --> FEATURE_API
    FEATURE_DI --> FEATURE_DEFAULT
    FEATURE_DI --> CORE

    FEATURE_API --> CORE
```
