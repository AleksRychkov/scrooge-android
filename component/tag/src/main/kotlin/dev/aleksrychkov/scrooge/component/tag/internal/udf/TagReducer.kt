package dev.aleksrychkov.scrooge.component.tag.internal.udf

import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
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
                            TagCommand.CreateNewTag(
                                name = state.searchQuery,
                            )
                        )
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

            is TagEvent.External.Restore -> {
                state.reduceWith(event) {
                    command {
                        listOf(TagCommand.Restore(tag = event.tag))
                    }
                }
            }

            TagEvent.External.Init -> {
                state.reduceWith(event) {
                    command {
                        listOf(TagCommand.ObserveTags)
                    }
                }
            }

            is TagEvent.External.Search -> {
                state.reduceWith(event) {
                    command {
                        listOf(
                            TagCommand.Search(
                                query = event.query,
                                tags = tags.toList(),
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
                                    tags = event.list
                                )
                            )
                        }
                    }
                    state {
                        copy(tags = event.list)
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

            is TagEvent.Internal.DeletedTag -> {
                state.reduceWith(event) {
                    effects {
                        val msg = String.format(
                            resourceManager.getString(resources.string.tag_deleted),
                            event.tag.name
                        )
                        listOf(
                            TagEffect.TagDeleted(
                                message = msg,
                                actionLabel = resourceManager.getString(
                                    resources.string.undo
                                ),
                                tag = event.tag,
                            )
                        )
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
                        val msg =
                            resourceManager.getString(
                                resources.string.tag_error_failed_to_delete
                            )
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }

            TagEvent.Internal.FailedToRestoreTag -> {
                state.reduceWith(event) {
                    effects {
                        val msg =
                            resourceManager.getString(
                                resources.string.tag_error_failed_to_restore
                            )
                        listOf(TagEffect.ShowInfoMessage(msg))
                    }
                }
            }

            TagEvent.Internal.FailedToObserveTags -> TODO()
        }
    }
}
