package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                MyApp()
            }
        }
    }
}

// Column 으로, 아이템 전체 요소를 Column 으로
@Composable
private fun MyApp() {
    /**
     * rememberSaveable을 사용해 configuration change에서도 살아남는 변수 생성
     * 이는 Bundle에 정보를 저장하고, restore 함!
     */
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    if (shouldShowOnboarding) {
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        Greetings()
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

// item 한개
@Composable
fun Greeting(name: String) {
    /**
    Compose는 Ui 안의 data가 변경되면 composable function을 호출하면서 데이터를 변경하는데
    이를 recomposition이라고 합니다. 이때마다 변수로 선언하면 변수가 계속 false로 불림
    이를 방지하기 위해 이전의 데이터를 유지하는 remember를 사용하면 reset 되지 않고 데이터를 유지시킬 수 있다!
     */
    val expanded = remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded.value) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                /** weight를 줌으로써 공간을 채우는 동시에 다른 요소를 밀어내서, 끝으로 보냄
                 * fillMaxWidth를 안써도 됨. -> 중복됨
                 */
                modifier = Modifier
                    .weight(1f)
                    /**
                     * padding 바텀으로 주기
                     */
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello, ")
                Text(
                    text = name, style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DefaultPreviewDark")
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greetings()
    }
}

// 온보딩 화면
@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelabs!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Cointinue")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MyApplicationTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}
