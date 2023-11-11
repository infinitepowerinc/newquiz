package com.infinitepower.newquiz.comparison_quiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.comparison_quiz.ui.AnimationState
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun ComparisonMidContent(
    modifier: Modifier = Modifier,
    questionPosition: Int,
    highestPosition: Int,
    verticalContent: Boolean,
    animationState: AnimationState
) {
    ComparisonMidContainer(
        modifier = modifier,
        verticalContent = verticalContent,
        currentPositionContent = {
            Text(
                text = stringResource(id = CoreR.string.position_n, questionPosition),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        highestPositionContent = {
            Text(
                text = stringResource(id = CoreR.string.highest_n, highestPosition),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        midContent = { MiddleCircle(animationState = animationState) }
    )
}

@Composable
private fun ComparisonMidContainer(
    modifier: Modifier = Modifier,
    verticalContent: Boolean,
    currentPositionContent: @Composable () -> Unit,
    highestPositionContent: @Composable () -> Unit,
    midContent: @Composable () -> Unit
) {
    if (verticalContent) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            currentPositionContent()
            midContent()
            highestPositionContent()
        }
    } else {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            currentPositionContent()
            midContent()
            highestPositionContent()
        }
    }
}

@Composable
@PreviewNightLight
private fun ComparisonMidContentPreview() {
    NewQuizTheme {
        Surface {
            ComparisonMidContent(
                questionPosition = 1,
                highestPosition = 10,
                verticalContent = true,
                modifier = Modifier.padding(16.dp),
                animationState = AnimationState.Normal
            )
        }
    }
}
