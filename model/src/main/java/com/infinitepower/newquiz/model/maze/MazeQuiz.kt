package com.infinitepower.newquiz.model.maze

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType

@Keep
data class MazeQuiz(
    val items: List<MazeItem>
) {
    sealed interface MazeItem {
        val id: Int
        val difficulty: QuestionDifficulty
        val played: Boolean

        @Keep
        data class Wordle(
            val word: String,
            val wordleQuizType: WordleQuizType,
            override val id: Int = 0,
            override val difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
            override val played: Boolean = false
        ) : MazeItem

        @Keep
        data class MultiChoice(
            val question: MultiChoiceQuestion,
            override val id: Int = 0,
            override val difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
            override val played: Boolean = false
        ) : MazeItem
    }
}

fun emptyMaze(): MazeQuiz = MazeQuiz(items = emptyList())

/**
 * Check if an item at a given index in a list of MazeItem objects is playable.
 * An item is considered playable if it has not been played and the previous item has been played.
 *
 * @param index The index of the item to check.
 * @return true if the current item has not been played and either the previous item does not exist or has been played
 */
fun List<MazeQuiz.MazeItem>.isPlayableItem(index: Int): Boolean {
    // Check if the current item has been played. If it has, return false.
    if (isItemPlayed(index)) return false

    // If the current item is the first item in the list, return true.
    if (index == 0) return true

    // If the current item is not the first item in the list, check if the previous item has been played.
    // If it has, return true. Otherwise, return false.
    return isItemPlayed(index - 1)
}

/**
 * Returns true if the item at the given index has been played, false otherwise.
 *
 * @param index The index of the item to check.
 * @return True if the item has been played, false otherwise.
 */
infix fun List<MazeQuiz.MazeItem>.isItemPlayed(
    index: Int
): Boolean = getOrNull(index)?.played == true