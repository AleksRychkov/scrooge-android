@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.category

import android.content.Context
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.category.internal.DefaultCategories
import dev.aleksrychkov.scrooge.feature.category.internal.DefaultCreateCategoryUseCase
import dev.aleksrychkov.scrooge.feature.category.internal.DefaultDeleteCategoryUseCase
import dev.aleksrychkov.scrooge.feature.category.internal.DefaultObserveCategoryUseCase
import dev.aleksrychkov.scrooge.feature.category.internal.DefaultPreloadCategoriesUseCase
import dev.aleksrychkov.scrooge.feature.category.internal.source.CategoryKeyValueSource
import kotlinx.coroutines.Dispatchers

fun buildCategoryModule(context: Context): NaiveModule {
    return module {
        val defaultCategories = DefaultCategories(context = context)
        factory<CategoryKeyValueSource> {
            CategoryKeyValueSource(
                storeName = "category_key_value",
                context = context,
            )
        }
        factory<DeleteCategoryUseCase> {
            DefaultDeleteCategoryUseCase(
                categoryDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<CreateCategoryUseCase> {
            DefaultCreateCategoryUseCase(
                categoryDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<ObserveCategoryUseCase> {
            DefaultObserveCategoryUseCase(
                categoryDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<PreloadCategoriesUseCase> {
            DefaultPreloadCategoriesUseCase(
                categoryDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
                defaultCategories = defaultCategories,
                keyValueSource = getLazy(),
            )
        }
    }
}
