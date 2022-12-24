package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.LogoQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.logo_quiz.LogoQuizBaseItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import java.security.SecureRandom
import kotlin.random.Random

@Singleton
class LogoQuizRepositoryImpl @Inject constructor() : LogoQuizRepository {
    private val remoteConfig = Firebase.remoteConfig

    override suspend fun getRandomQuestions(
        amount: Int,
        category: Int?,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> {
        val allLogos = getRemoteConfigAllLogos()

        val filteredByDifficulty = if (difficulty != null) {
            allLogos.filter { item -> item.difficulty == difficulty }
        } else allLogos

        return filteredByDifficulty
            .sortedBy { it.name }
            .shuffled(random)
            .take(amount)
            .map { item -> item.toQuestion() }
    }

    private fun getRemoteConfigAllLogos(): List<LogoQuizBaseItem> {
        val allLogosQuizStr = remoteConfig.getString("all_logos_quiz")
        return Json.decodeFromString(allLogosQuizStr)
    }

    private fun LogoQuizBaseItem.toQuestion(
        random: Random = Random
    ): MultiChoiceQuestion {

        val answerCountries = incorrectAnswers.shuffled(random) + name
        val answers = answerCountries.shuffled(random)

        return MultiChoiceQuestion(
            id = random.nextInt(),
            description = description,
            imageUrl = imgUrl,
            answers = answers,
            correctAns = answers.indexOf(name),
            category = "Logo Quiz",
            difficulty = difficulty,
            lang = "en",
            type = "multiple"
        )
    }
}