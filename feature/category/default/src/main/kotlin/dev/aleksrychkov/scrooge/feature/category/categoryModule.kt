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
import kotlinx.coroutines.Dispatchers

fun buildCategoryModule(context: Context): NaiveModule {
    return module {
        val defaultCategories = DefaultCategories(context = context)
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
                defaultCategories = defaultCategories,
            )
        }
        factory<ObserveCategoryUseCase> {
            DefaultObserveCategoryUseCase(
                categoryDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
                defaultCategories = defaultCategories,
            )
        }
    }
}
