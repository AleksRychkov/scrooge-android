package dev.aleksrychkov.scrooge.component.currency

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.aleksrychkov.scrooge.component.currency.internal.CurrencyComponentInternal
import dev.aleksrychkov.scrooge.component.currency.internal.entity.FavoriteCurrencyEntity
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyState
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsTabBar
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.ListItemHeight
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun CurrencyContent(
    modifier: Modifier,
    component: CurrencyComponent,
    callback: (CurrencyEntity?) -> Unit,
) {
    CurrencyContent(
        modifier = modifier,
        component = component as CurrencyComponentInternal,
        callback = callback,
    )
}

@Composable
private fun CurrencyContent(
    modifier: Modifier,
    component: CurrencyComponentInternal,
    callback: (CurrencyEntity?) -> Unit,
) {
    val state by component.state.collectAsStateWithLifecycle()

    CurrencyContent(
        modifier = modifier,
        state = state,
        selectCurrency = callback,
        removeFromFavorite = component::removeFromFavorite,
        addToFavorite = component::addToFavorite,
    )
}

@Composable
private fun CurrencyContent(
    modifier: Modifier,
    state: CurrencyState,
    selectCurrency: (CurrencyEntity?) -> Unit,
    removeFromFavorite: (CurrencyEntity) -> Unit,
    addToFavorite: (CurrencyEntity) -> Unit,
) {
    if (state.favorite == null || state.all == null) return
    var tabIndex by remember {
        val index = if (state.favorite.isNotEmpty()) 0 else 1
        mutableIntStateOf(index)
    }
    val titles = listOf(
        stringResource(Resources.string.currency_tab_favorite),
        stringResource(Resources.string.currency_tab_all),
    )

    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        DsTabBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal),
            options = titles,
            selectedIndex = tabIndex,
            onOptionSelected = { tabIndex = it }
        )

        CurrencyList(
            modifier = Modifier.fillMaxSize(),
            tabIndex = tabIndex,
            state = state,
            selectCurrency = selectCurrency,
            removeFromFavorite = removeFromFavorite,
            addToFavorite = addToFavorite,
        )
    }
}

@Composable
private fun CurrencyList(
    modifier: Modifier,
    tabIndex: Int,
    state: CurrencyState,
    selectCurrency: (CurrencyEntity?) -> Unit,
    removeFromFavorite: (CurrencyEntity) -> Unit,
    addToFavorite: (CurrencyEntity) -> Unit,
) {
    if (state.favorite == null || state.all == null) return
    val items = if (tabIndex == 0) {
        state.favorite
    } else {
        state.all
    }

    Box(
        modifier = modifier.padding(bottom = Normal)
    ) {
        val listState = rememberLazyListState()

        LaunchedEffect(tabIndex) {
            listState.scrollToItem(0)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            state = listState,
        ) {
            items(
                items = items,
                key = { item -> item.currency.currencyNumCode }
            ) { item ->
                CurrencyItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .debounceClickable {
                            selectCurrency(item.currency)
                        },
                    item = item,
                    removeFromFavorite = removeFromFavorite,
                    addToFavorite = addToFavorite,
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    modifier: Modifier,
    item: FavoriteCurrencyEntity,
    removeFromFavorite: (CurrencyEntity) -> Unit,
    addToFavorite: (CurrencyEntity) -> Unit,
) {
    Row(
        modifier = modifier.height(ListItemHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CurrencyImage(
            modifier = Modifier.padding(Large),
            currency = item.currency
        )
        Text(
            modifier = Modifier,
            text = item.currency.currencySymbol,
            fontStyle = FontStyle.Italic,
        )
        Text(
            modifier = Modifier.padding(start = Normal),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = item.currency.currencyName
        )
        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

        Box(
            modifier = Modifier
                .height(ListItemHeight)
                .padding(Normal)
                .aspectRatio(1f)
                .clip(CircleShape)
                .debounceClickable {
                    if (item.isFavorite) {
                        removeFromFavorite(item.currency)
                    } else {
                        addToFavorite(item.currency)
                    }
                }
                .padding(Medium),
            contentAlignment = Alignment.Center,
        ) {
            val iconImage = if (item.isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            }
            val iconTint = if (item.isFavorite) {
                MaterialTheme.colorScheme.error
            } else {
                LocalContentColor.current
            }
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = iconImage,
                contentDescription = null,
                tint = iconTint,
            )
        }
    }
}

@Composable
private fun CurrencyImage(
    modifier: Modifier,
    currency: CurrencyEntity? = null
) {
    AsyncImage(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(currency?.flagUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}
