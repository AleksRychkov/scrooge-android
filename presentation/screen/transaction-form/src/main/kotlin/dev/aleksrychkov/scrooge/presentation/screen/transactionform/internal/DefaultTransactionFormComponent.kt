package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.router.context.RouterComponentContext
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormComponent as FormComponent

@Suppress("UnusedPrivateProperty", "TooManyFunctions")
internal class DefaultTransactionFormComponent(
    private val componentContext: ComponentContext,
    override val transactionId: Long? = null,
    override val transactionType: TransactionType,
) : TransactionFormComponentInternal, ComponentContext by componentContext {

    private val router: Router by lazy {
        (componentContext as RouterComponentContext).router
    }

    private val _formComponent: FormComponent by lazy {
        FormComponent(
            componentContext = childContext("TransactionFormFormComponent"),
            transactionId = transactionId,
            type = transactionType,
        )
    }

    override val formComponent: FormComponent
        get() = _formComponent

    override fun onBackClicked() {
        router.close()
    }

    override fun onSaveClicked() {
        _formComponent.onSaveClicked()
    }
}
