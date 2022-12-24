package com.infinitepower.newquiz.math_quiz.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.multi_choice_quiz.MultiChoiceQuizType
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun getMathQuizListCardItemData(
    navigator: DestinationsNavigator
) = listOf(
    HomeCardItem.GroupTitle(
        title = R.string.guess_number,
    ),
    HomeCardItem.LargeCard(
        title = R.string.guess_number,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = {
            navigator.navigate(WordleScreenDestination(quizType = WordleQuizType.NUMBER))
        }
    ),
    HomeCardItem.GroupTitle(
        title = R.string.guess_math_formula,
    ),
    HomeCardItem.LargeCard(
        title = R.string.guess_math_formula,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = {
            navigator.navigate(WordleScreenDestination(quizType = WordleQuizType.MATH_FORMULA))
        }
    ),
    HomeCardItem.GroupTitle(
        title = R.string.guess_solution,
    ),
    HomeCardItem.LargeCard(
        title = R.string.guess_solution,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = {
            navigator.navigate(MultiChoiceQuizScreenDestination(type = MultiChoiceQuizType.GUESS_MATH_SOLUTION))
        }
    )
)