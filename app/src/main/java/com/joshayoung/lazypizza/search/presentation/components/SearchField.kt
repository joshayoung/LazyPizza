package com.joshayoung.lazypizza.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.SearchIcon
import com.joshayoung.lazypizza.ui.theme.surfaceHigher

@Composable
fun SearchField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Box(
        modifier =
            modifier
                .shadow(4.dp, shape = RoundedCornerShape(28.dp), clip = true)
                .background(MaterialTheme.colorScheme.surfaceHigher, RoundedCornerShape(28.dp))
                .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = SearchIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                state = state,
                lineLimits = TextFieldLineLimits.SingleLine,
                decorator = { innerBox ->
                    if (state.text.isEmpty() && !isFocused) {
                        Text(
                            "Search for delicious food…",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    innerBox()
                },
                modifier =
                    Modifier
                        .onFocusChanged {
                            isFocused = it.isFocused
                        }
            )
        }
    }
}

@Preview
@Composable
fun SearchFieldPreview() {
    LazyPizzaTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
        ) {
            SearchField(state = TextFieldState())
        }
    }
}