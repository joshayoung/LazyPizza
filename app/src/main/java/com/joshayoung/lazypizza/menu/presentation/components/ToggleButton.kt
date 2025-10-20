package com.joshayoung.lazypizza.menu.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme

@Composable
fun ToggleButton(
    imageResource: Int,
    click: () -> Unit,
    preventMore: MutableState<Boolean>? = null
) {
    var imageColor: Color = Color.DarkGray
    if (preventMore != null) {
        if (preventMore.value) {
            imageColor = MaterialTheme.colorScheme.outline
        }
    }

    Box(
        modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(10.dp)
                )
    ) {
        IconButton(
            onClick = {
                click()
            },
            modifier =
                Modifier
                    .size(30.dp)
        ) {
            Icon(
                painter = painterResource(imageResource),
                tint = imageColor,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleButtonPreview() {
    val preventMoreNo = remember { mutableStateOf(false) }
    val preventMoreYes = remember { mutableStateOf(true) }

    LazyPizzaTheme {
        Column {
            ToggleButton(
                R.drawable.plus,
                click = {},
                preventMore = preventMoreNo
            )
            ToggleButton(
                R.drawable.plus,
                click = {},
                preventMore = preventMoreYes
            )
        }
    }
}
