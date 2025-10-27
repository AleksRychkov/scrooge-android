package dev.aleksrychkov.scrooge.component.tag.internal

import com.arkivanov.decompose.ComponentContext

internal class DefaultTagComponent(
    private val componentContext: ComponentContext,
) : TagComponentInternal, ComponentContext by componentContext
