package dev.aleksrychkov.scrooge.presentation.component.tags.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList
import dev.aleksrychkov.scrooge.core.resources.R as resources

internal class TagReducer(
    private val resourceManager: ResourceManager = get(),
) : Reducer<TagState, TagEvent, TagCommand, TagEffect> {

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun reduce(
        event: TagEvent,
        state: TagState
    ): ReducerResult<TagState, TagCommand, TagEffect> {
        return when (event) {
            TagEvent.External.AddNewTag -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            TagCommand.Create(
                                name = state.searchQuery,
                            )
                        )
                    }
                    state {
                        copy(searchQuery = state.searchQuery.trim())
                    }
                }
            }

            is TagEvent.External.Delete -> {
                state.reduceWith(event) {
                    command {
                        listOf(TagCommand.Delete(tag = event.tag))
                    }
                }
            }

            TagEvent.External.Init -> {
                state.reduceWith(event) {
                    command {
                        listOf(TagCommand.Observe)
                    }
                }
            }

            is TagEvent.External.Search -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            TagCommand.Search(
                                query = event.query,
                                tags = tags.toSet(),
                            )
                        )
                    }
                    state {
                        copy(searchQuery = event.query)
                    }
                }
            }

            is TagEvent.Internal.Tags -> {
                state.reduceWith(event) {
                    if (state.searchQuery.isNotBlank()) {
                        command {
                            listOf(
                                TagCommand.Search(
                                    query = state.searchQuery,
                                    tags = event.set
                                )
                            )
                        }
                    }
                    state {
                        copy(tags = event.set.toImmutableList())
                    }
                }
            }

            is TagEvent.Internal.Filtered -> {
                state.reduceWith(event) {
                    state {
                        copy(filtered = event.list)
                    }
                }
            }

            TagEvent.Internal.FailedToCreateNewTag -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(
                                resources.string.tag_error_failed_to_create
                            )
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }

            is TagEvent.Internal.FailedToCreateNewTagDuplicate -> {
                state.reduceWith(event) {
                    effects {
                        val msg = String.format(
                            resourceManager.getString(resources.string.tag_error_duplicate),
                            event.tag.name
                        )
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }

            TagEvent.Internal.FailedToCreateNewTagEmptyName -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(resources.string.tag_error_empty_name)
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }

            TagEvent.Internal.FailedToDeleteTag -> {
                state.reduceWith(event) {
                    effects {
                        val msg = resourceManager.getString(
                            resources.string.tag_error_failed_to_delete
                        )
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }
        }
    }
}
