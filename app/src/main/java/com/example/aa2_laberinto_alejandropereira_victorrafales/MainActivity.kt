package com.example.aa2_laberinto_alejandropereira_victorrafales

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aa2_laberinto_alejandropereira_victorrafales.ui.theme.AA2_Laberinto_AlejandroPereira_VictorRafalesTheme


private var x by mutableFloatStateOf(0f)
private var y by mutableFloatStateOf(0f)
private var z by mutableFloatStateOf(0f)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(x,y,z,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SensorScreen(x:Float , y: Float, z:Float, modifier: Modifier){
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = x.toString(), onValueChange = {}, label = {Text("X")}, readOnly = true)
        TextField(value = y.toString(), onValueChange = {}, label = {Text("Y")}, readOnly = true)
        TextField(value = z.toString(), onValueChange = {}, label = {Text("Z")}, readOnly = true)
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(x,y,z, modifier = Modifier)
    }
}

