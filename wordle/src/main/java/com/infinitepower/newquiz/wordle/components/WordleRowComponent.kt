package com.infinitepower.newquiz.wordle.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.CustomColor
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.extendedColors
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Composable
internal fun WordleRowComponent(
    modifier: Modifier = Modifier,
    word: String,
    wordleRowItem: WordleRowItem,
    isPreview: Boolean = false,
    isColorBlindEnabled: Boolean = false,
    isLetterHintsEnabled: Boolean = false,
    onItemClick: (index: Int) -> Unit
) {
    val presentItems = wordleRowItem
        .items
        .filterIsInstance<WordleItem.Present>()

    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
    ) {
        wordleRowItem.items.forEachIndexed { index, item ->
            val wordCharCount = word.count { wordChar ->
                wordChar == item.char.value && item is WordleItem.Present
            }

            val itemCharCount = presentItems.count { presentItem ->
                presentItem.char == item.char && item is WordleItem.Present
            }

            WordleComponent(
                item = item,
                enabled = !isPreview,
                isColorBlindEnabled = isColorBlindEnabled,
                onClick = { onItemClick(index) },
                charCount = wordCharCount,
                isLetterHintsEnabled = isLetterHintsEnabled && wordCharCount != itemCharCount
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun WordleComponent(
    modifier: Modifier = Modifier,
    item: WordleItem,
    enabled: Boolean = true,
    isColorBlindEnabled: Boolean = false,
    isLetterHintsEnabled: Boolean = false,
    charCount: Int = 0,
    onClick: () -> Unit
) {
    if (charCount > 1 && isLetterHintsEnabled) {
        BadgedBox(
            badge = {
                Badge {
                    val badgeNumber = charCount.toString()
                    Text(
                        badgeNumber,
                        modifier = Modifier.semantics {
                            contentDescription = "$badgeNumber new notifications"
                        }
                    )
                }
            }
        ) {
            WordleComponentImpl(
                modifier = modifier,
                item = item,
                enabled = enabled,
                isColorBlindEnabled = isColorBlindEnabled,
                onClick = onClick
            )
        }
    } else {
        WordleComponentImpl(
            modifier = modifier,
            item = item,
            enabled = enabled,
            isColorBlindEnabled = isColorBlindEnabled,
            onClick = onClick
        )
    }
}

@Composable
@ExperimentalMaterial3Api
fun WordleComponentImpl(
    modifier: Modifier = Modifier,
    item: WordleItem,
    enabled: Boolean = true,
    isColorBlindEnabled: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = getItemRowBackgroundColor(
        item = item,
        isColorBlindEnabled = isColorBlindEnabled
    )
    val backgroundColorAnimated by animateColorAsState(targetValue = backgroundColor)

    val textColor = getItemRowTextColor(
        item = item,
        isColorBlindEnabled = isColorBlindEnabled
    )
    val textColorAnimated by animateColorAsState(targetValue = textColor)

    val elevation = if (item.char.isEmpty()) 0.dp else 8.dp
    val elevationAnimated by animateDpAsState(targetValue = elevation)

    val stroke = if (item.char.isEmpty()) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    } else null

    val itemDescription = item.getItemDescription()

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .size(50.dp)
            .testTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .semantics {
                contentDescription = itemDescription
            },
        color = backgroundColorAnimated,
        tonalElevation = elevationAnimated,
        border = stroke,
        onClick = onClick,
        enabled = item is WordleItem.None && enabled
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.char.toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = textColorAnimated
            )
        }
    }
}

@Composable
@ReadOnlyComposable
internal fun getItemRowBackgroundColor(
    item: WordleItem,
    isColorBlindEnabled: Boolean
): Color {
    val backgroundCorrectColor = MaterialTheme.extendedColors.getColorAccentByKey(
        key = if (isColorBlindEnabled) {
            CustomColor.Keys.Blue
        } else CustomColor.Keys.Green
    )

    val backgroundPresentColor = MaterialTheme.extendedColors.getColorAccentByKey(
        key = if (isColorBlindEnabled) {
            CustomColor.Keys.Red
        } else CustomColor.Keys.Yellow
    )

    return when (item) {
        is WordleItem.Correct -> backgroundCorrectColor
        is WordleItem.Present -> backgroundPresentColor
        else -> MaterialTheme.colorScheme.surface
    }
}

@Composable
@ReadOnlyComposable
internal fun getItemRowTextColor(
    item: WordleItem,
    isColorBlindEnabled: Boolean
): Color {
    val backgroundCorrectColor = MaterialTheme.extendedColors.getColorOnAccentByKey(
        key = if (isColorBlindEnabled) {
            CustomColor.Keys.Blue
        } else CustomColor.Keys.Green
    )

    val backgroundPresentColor = MaterialTheme.extendedColors.getColorOnAccentByKey(
        key = if (isColorBlindEnabled) {
            CustomColor.Keys.Red
        } else CustomColor.Keys.Yellow
    )

    return when (item) {
        is WordleItem.Correct -> backgroundCorrectColor
        is WordleItem.Present -> backgroundPresentColor
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
@ReadOnlyComposable
private fun WordleItem.getItemDescription() = when (this) {
    is WordleItem.Empty -> stringResource(id = CoreR.string.item_empty)
    is WordleItem.None -> stringResource(id = CoreR.string.item_i_none, this.char)
    is WordleItem.Present -> stringResource(id = CoreR.string.item_i_present, this.char)
    is WordleItem.Correct -> stringResource(id = CoreR.string.item_i_correct, this.char)
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal object WordleRowComponentTestingTags {
    const val WORDLE_COMPONENT_SURFACE = "WORDLE_COMPONENT_SURFACE"
}

@Composable
@PreviewNightLight
private fun WordleComponentPreview() {
    val item = WordleItem.fromChar('A')

    NewQuizTheme {
        Surface {
            WordleComponent(
                modifier = Modifier.padding(16.dp),
                item = item,
                onClick = {}
            )
            WordleComponent(
                modifier = Modifier.padding(16.dp),
                item = item,
                onClick = {}
            )
        }
    }
}

@Composable
@PreviewNightLight
private fun WordleRowComponentPreview() {
    val item = WordleRowItem(
        items = listOf(
            WordleItem.Empty,
            WordleItem.None(char = WordleChar('A')),
            WordleItem.Present(char = WordleChar('B')),
            WordleItem.Correct(char = WordleChar('C')),
            WordleItem.Present(char = WordleChar('B')),
        )
    )

    NewQuizTheme {
        Surface {
            WordleRowComponent(
                modifier = Modifier.padding(16.dp),
                word = "QUIZ",
                wordleRowItem = item,
                onItemClick = {},
                isLetterHintsEnabled = true
            )
        }
    }
}