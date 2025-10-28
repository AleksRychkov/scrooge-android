package dev.aleksrychkov.scrooge.component.currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.component.currency.internal.CurrencyComponentInternal
import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyState
import dev.aleksrychkov.scrooge.core.designsystem.composables.NavigationBarSpacer
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
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
        setSearchQuery = component::setSearchQuery,
    )
}

@Composable
private fun CurrencyContent(
    modifier: Modifier,
    state: CurrencyState,
    selectCurrency: (CurrencyEntity?) -> Unit,
    setSearchQuery: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .displayCutoutPadding()
            .statusBarsPadding()
    ) {
        CurrencyBar(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            setSearchQuery = setSearchQuery,
        )

        Spacer(modifier = Modifier.height(Normal))

        CurrenciesList(
            modifier = Modifier.fillMaxSize(),
            state = state,
            selectCurrency = selectCurrency,
        )
    }
}

@Composable
private fun CurrenciesList(
    modifier: Modifier,
    state: CurrencyState,
    selectCurrency: (CurrencyEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        val currencies = if (state.searchQuery.isNotBlank()) {
            state.filtered
        } else {
            state.currencies
        }

        items(
            items = currencies,
            key = { currency -> currency.currencyCode }
        ) { currency ->
            Currency(
                modifier = Modifier.animateItem(),
                value = currency,
                selectCurrency = selectCurrency,
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
private fun Currency(
    modifier: Modifier = Modifier,
    value: CurrencyEntity,
    selectCurrency: (CurrencyEntity) -> Unit,
) {
    val itemHeight = 60.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = itemHeight)
            .clickable {
                selectCurrency(value)
            }
            .padding(start = Large),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(weight = 1f, fill = true),
            text = value.name,
        )
    }
}

@Composable
private fun CurrencyBar(
    modifier: Modifier,
    state: CurrencyState,
    setSearchQuery: (String) -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Normal),
            value = state.searchQuery,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            trailingIcon = {
                if (state.searchQuery.isNotBlank()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Resources.string.clear),
                        modifier = Modifier
                            .clickable { setSearchQuery("") },
                    )
                }
            },
            onValueChange = setSearchQuery,
        )
    }
}
