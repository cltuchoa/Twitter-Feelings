package com.uchoa.sentimentanalizer.core

import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.language.v1.CloudNaturalLanguage
import com.google.api.services.language.v1.CloudNaturalLanguageRequestInitializer
import com.google.api.services.language.v1.model.AnnotateTextRequest
import com.google.api.services.language.v1.model.AnnotateTextResponse
import com.google.api.services.language.v1.model.Document
import com.google.api.services.language.v1.model.Features
import com.uchoa.sentimentanalizer.model.UserSentiment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SentimentAnalyzer(apiKey: String) {

    private var moodAnalyzer = MoodAnalyzer()
    private var disposable: Disposable? = null

    private val naturalLanguageService: CloudNaturalLanguage = CloudNaturalLanguage.Builder(
        AndroidHttp.newCompatibleTransport(),
        AndroidJsonFactory(), null
    ).setCloudNaturalLanguageRequestInitializer(
        CloudNaturalLanguageRequestInitializer(apiKey)
    ).build()

    fun analyze(text: String, sentimentListener: SentimentListener) {
        val document = Document()
        document.type = "PLAIN_TEXT"
        document.content = text

        val features = Features()
        features.extractEntities = true
        features.extractDocumentSentiment = true

        val request = AnnotateTextRequest()
        request.document = document
        request.features = features

        disposable = Single.fromCallable { getSentiment(request) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error -> handleServiceError(error, sentimentListener) }
            .doAfterSuccess { disposable?.dispose() }
            .subscribe { result ->
                handleServiceSuccess(result, sentimentListener)
            }
    }

    private fun handleServiceError(
        error: Throwable,
        sentimentListener: SentimentListener
    ) {
        Log.e("SentimentAnalyzer", "handleServiceError - ${error.message}")
        sentimentListener.onAnalizerFailed()
    }

    private fun handleServiceSuccess(
        response: AnnotateTextResponse?,
        sentimentListener: SentimentListener
    ) {
        if (response != null) {
            val sentiment = response.documentSentiment
            val mood = moodAnalyzer.getMood(sentiment.score)
            val userSentiment = UserSentiment(mood, sentiment.score, sentiment.magnitude)
            sentimentListener.onAnalizerSuccess(userSentiment)
        } else {
            sentimentListener.onAnalizerFailed()
        }
    }

    private fun getSentiment(request: AnnotateTextRequest): AnnotateTextResponse {
        return naturalLanguageService.documents().annotateText(request).execute()
    }
}
