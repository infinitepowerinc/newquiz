package com.infinitepower.newquiz.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.logging.CoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.translation_dynamic_feature.TranslatorUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppStartLoggingAnalyticsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val coreLoggingAnalytics: CoreLoggingAnalytics,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val translatorUtil: TranslatorUtil
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val wordleLang = settingsDataStoreManager.getPreference(SettingsCommon.InfiniteWordleQuizLanguage)
        coreLoggingAnalytics.setWordleLangUserProperty(wordleLang)

        val isTranslatorModelDownloaded = translatorUtil.isModelDownloaded()
        coreLoggingAnalytics.setTranslatorModelDownloaded(isTranslatorModelDownloaded)

        return Result.success()
    }
}