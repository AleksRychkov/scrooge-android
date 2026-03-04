package dev.aleksrychkov.scrooge.presentation.screen.root.internal.transfer

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase
import kotlinx.coroutines.CompletableDeferred

internal class DefaultGetExportUriUseCase(
    resultCaller: ActivityResultCaller,
) : GetExportUriUseCase {

    private companion object {
        const val DB_MIME_TYPE = "application/octet-stream"
    }

    private val callback: ActivityResultCallback<Uri?> = ActivityResultCallback<Uri?> { uri ->
        deferred?.complete(uri?.toString())
    }
    private var deferred: CompletableDeferred<String?>? = null

    private var getUriLauncher = resultCaller.registerForActivityResult(
        contract = ActivityResultContracts.CreateDocument(DB_MIME_TYPE),
        callback = callback
    )

    override suspend fun invoke(fileName: String): String? {
        deferred = CompletableDeferred()
        getUriLauncher.launch(fileName)

        return deferred?.await()
    }
}
