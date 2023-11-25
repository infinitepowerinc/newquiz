package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.di.LocalUserDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUserServiceImpl @Inject constructor(
    @LocalUserDataStoreManager private val dataStoreManager: DataStoreManager,
    private val remoteConfig: RemoteConfig,
    private val gameResultDao: GameResultDao,
    private val multiChoiceXpGenerator: MultiChoiceQuizXpGenerator,
    private val wordleXpGenerator: WordleXpGenerator
) : LocalUserService {
    override suspend fun userAvailable(): Boolean {
        val uid = dataStoreManager.getPreference(LocalUserCommon.UserUid)
        return uid.isNotBlank()
    }

    override suspend fun getUser(): User? {
        val uid = dataStoreManager.getPreference(LocalUserCommon.UserUid)
        if (uid.isBlank()) return null

        val totalXp = dataStoreManager.getPreference(LocalUserCommon.UserTotalXp)

        val diamonds = getUserDiamonds()

        return User(
            uid = uid,
            totalXp = totalXp.toULong(),
            diamonds = diamonds
        )
    }

    override suspend fun getUserDiamonds(): UInt {
        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)
        val diamonds = dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(initialDiamonds))

        return diamonds.toUInt()
    }

    override suspend fun addRemoveDiamonds(diamonds: Int) {
        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)
        val currentDiamonds =
            dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(initialDiamonds))
        val newDiamonds = currentDiamonds + diamonds

        dataStoreManager.editPreference(
            key = LocalUserCommon.UserDiamonds(initialDiamonds).key,
            newValue = newDiamonds
        )
    }

    suspend fun updateNewLevelDiamonds() {
        val newLevelDiamonds = remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS)

        addRemoveDiamonds(newLevelDiamonds)
    }

    private fun List<MultiChoiceQuestionStep.Completed>.getAverageQuizTime(): Double {
        return map(MultiChoiceQuestionStep.Completed::questionTime).average()
    }

    private suspend fun saveNewXP(newXp: UInt) {
        val currentUser = getUser() ?: throw IllegalStateException("User not found")

        val newTotalXp = currentUser.totalXp + newXp

        // Save the new total xp
        dataStoreManager.editPreference(
            key = LocalUserCommon.UserTotalXp.key,
            newValue = newTotalXp.toLong()
        )

        // Check if the user is in a new level
        val isNewLevel = currentUser.isNewLevel(newXp = newXp.toULong())

        // If is new level, update the user diamonds
        if (isNewLevel) {
            updateNewLevelDiamonds()
        }
    }

    override suspend fun saveMultiChoiceGame(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        generateXp: Boolean
    ) {
        var newXp = 0u

        // Generate xp if needed
        if (generateXp) {
            // Generate and get the new xp
            newXp = multiChoiceXpGenerator.generateXp(questionSteps)
            saveNewXP(newXp)
        }

        // Save the game result
        val correctAnswers = questionSteps.count { it.correct }
        val averageAnswerTime = questionSteps.getAverageQuizTime()

        gameResultDao.insertMultiChoiceResult(
            MultiChoiceGameResultEntity(
                correctAnswers = correctAnswers,
                questionCount = questionSteps.size,
                averageAnswerTime = averageAnswerTime,
                earnedXp = newXp.toInt(),
                playedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun saveWordleGame(
        wordLength: UInt,
        rowsUsed: UInt,
        maxRows: Int,
        categoryId: String,
        generateXp: Boolean
    ) {
        var newXp = 0u

        // Generate xp if needed
        if (generateXp) {
            // Generate and get the new xp
            newXp = wordleXpGenerator.generateXp(rowsUsed)
            saveNewXP(newXp)
        }

        // Save the game result
        gameResultDao.insertWordleResult(
            WordleGameResultEntity(
                earnedXp = newXp.toInt(),
                playedAt = System.currentTimeMillis(),
                wordLength = wordLength.toInt(),
                rowsUsed = rowsUsed.toInt(),
                maxRows = maxRows,
                categoryId = categoryId
            )
        )
    }
}