package com.eduardodenis.sorteiamegasena

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eduardodenis.sorteiamegasena.ui.theme.SorteiaMegaSenaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SorteiaMegaSenaTheme {
                MainApp()
            }
        }
    }
}


@Composable
fun MainApp() {

    val context = LocalContext.current
    val prefs = PreferencesManager(context)
    val bet = remember { mutableStateOf("") }
    val result = remember {
        mutableStateOf(prefs.getData(PREFS_KEY))
    }
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Boa Sorte", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp,
                    color = Color(0, 100, 0)
                ), modifier = Modifier.padding(top = 80.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = bet.value,
                    label = { Text("Insira um valor entre 6 e 15") },
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            Toast.makeText(context, "bota done clicado", Toast.LENGTH_LONG).show()
                            if (!validTextField(bet.value)) {
                                Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG)
                                    .show()
                                return@KeyboardActions
                            }
                            result.value = generatorNumber(bet.value.toInt())

                        }
                    ),
                    onValueChange = {
                        bet.value = validInput(it)
                    })

                Text(text = result.value)
            }

            Button(
                onClick = {
                    focusRequester.requestFocus()

                    if (!validTextField(bet.value)) {
                        Toast.makeText(context, "Preencha os campos", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    result.value = generatorNumber(bet.value.toInt())
                    prefs.saveData(PREFS_KEY, result.value)
                }, modifier = Modifier.padding(top = 70.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0, 100, 0),
                    contentColor = Color(255,255,255)
                )
            ) {
                Text(
                    text = "Gerar Números", style = TextStyle(
                        fontSize = 30.sp
                    )
                )
            }
        }
    }
}


const val PREFS_KEY = "key_mega"

fun validInput(input: String): String {
    return input.filter { it.isDigit() }
}

fun validTextField(qtd: String): Boolean {
    return !(qtd.isEmpty() || qtd.toInt() < 6 || qtd.toInt() > 15)
}

fun generatorNumber(qtd: Int): String {
    val listNumber = mutableSetOf<Int>()

    while (true) {
        val random = Random.nextInt(1, 60)
        listNumber.add(random + 1)
        if (listNumber.size == qtd) {
            break
        }
    }
    return listNumber.joinToString(" - ")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SorteiaMegaSenaTheme {
        MainApp()

    }
}