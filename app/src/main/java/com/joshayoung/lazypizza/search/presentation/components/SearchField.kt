package com.joshayoung.lazypizza.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.SearchIcon

@Composable
fun SearchField(state: TextFieldState) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerBox ->
            Row {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                ) {
                    if (state.text.isEmpty() && !isFocused) {
                        Text("Search for delicious food…")
                    }
                    innerBox()
                }
            }
        },
        modifier =
            Modifier
                .padding(vertical = 10.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                }.border(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                ).shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(10.dp)
                .fillMaxWidth()
    )
}

@Preview
@Composable
fun SearchFieldPreview() {
    LazyPizzaTheme {
        SearchField(state = TextFieldState())
    }
}