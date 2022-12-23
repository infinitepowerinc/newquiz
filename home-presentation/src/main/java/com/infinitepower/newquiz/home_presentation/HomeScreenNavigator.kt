package com.infinitepower.newquiz.home_presentation

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

interface HomeScreenNavigator {
    fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion> = arrayListOf())

    fun navigateToFlagQuiz()

    fun navigateToLogoQuiz()

    fun navigateToSettings()

    fun navigateToSavedQuestions()

    fun navigateToWordle()

    fun navigateToMaze()
}

internal class HomeNavigatorPreviewImpl : HomeScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        println("Navigating to Quick Quiz")
    }

    override fun navigateToFlagQuiz() {
        println("Navigating to Flag Quiz")
    }

    override fun navigateToLogoQuiz() {
        println("Navigating to Logo Quiz")
    }

    override fun navigateToSettings() {
        println("Navigating to Settings")
    }

    override fun navigateToSavedQuestions() {
        println("Navigating to Saved Questions")
    }

    override fun navigateToWordle() {
        println("Navigating to Wordle")
    }

    override fun navigateToMaze() {
        println("Navigating to Maze")
    }
}