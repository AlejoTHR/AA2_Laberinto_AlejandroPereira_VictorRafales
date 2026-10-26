package com.example.aa2_laberinto_alejandropereira_victorrafales

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aa2_laberinto_alejandropereira_victorrafales.ui.theme.AA2_Laberinto_AlejandroPereira_VictorRafalesTheme
import kotlinx.coroutines.delay


private var x by mutableFloatStateOf(0f)
private var y by mutableFloatStateOf(0f)
private var z by mutableFloatStateOf(0f)

private var r by mutableFloatStateOf(0f)
private var p by mutableFloatStateOf(0f)
private var yw by mutableFloatStateOf(0f)


private lateinit var sensorManager: SensorManager

class MainActivity : ComponentActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val Accsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if(Accsensor != null){
            sensorManager.registerListener(this, Accsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }



        enableEdgeToEdge()
        setContent {
            AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    SimpleGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onSensorChanged(event : SensorEvent) {
        x = event.values[0]
        y = event.values[1]
        z = event.values[2]

    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}

@Composable
fun SensorScreen(x:Float , y: Float, z:Float, modifier: Modifier){
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = x.toString(), onValueChange = {}, label = {Text("X")}, readOnly = true)
        TextField(value = y.toString(), onValueChange = {}, label = {Text("Y")}, readOnly = true)
        TextField(value = z.toString(), onValueChange = {}, label = {Text("Z")}, readOnly = true)

    }
}
@Composable
fun ShowTime(){
    val starTime = remember { SystemClock.elapsedRealtime() }
    var elapsed by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true){
            delay(100)
            elapsed = SystemClock.elapsedRealtime()-starTime
        }
    }
    Text("Tiempo: ${elapsed}")

}


@Composable
fun SimpleGame(modifier: Modifier) {
    var circleX by remember { mutableFloatStateOf(16f) }
    var speedX by remember { mutableFloatStateOf(0f) }

    var moveLeft by remember { mutableStateOf(false) }
    var moveRight by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(16)
        speedX = 0f
        if (moveLeft) {
            speedX = -5f
        }
        if (moveRight) {
            speedX = 5f
        }
        circleX += speedX

    }

    Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
        detectTapGestures { offset ->
            val w = size.width
            moveRight = false

            moveLeft = false

            if (offset.x < w / 3) {
                moveLeft = true

            } else if (offset.x > 2 * w / 3) {
                moveRight = true
            }

        }

    }

    ) {
        drawCircle(
            color = Color.Red, radius = 40f,
            center = Offset(circleX, size.height)
        )
    }
}


@Composable
fun CustomCanvas(){

    Canvas(modifier = Modifier.size(width = 200.dp, height = 400.dp)){
        drawRect(
            color = Color.Blue,
            size = Size(200.dp.toPx(), height = 100.dp.toPx())
        )

        drawCircle(
            color = Color.Yellow,
            radius = 250.dp.toPx(),
            center = Offset(40.dp.toPx(), 200.dp.toPx())
        )
    }


}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(x,y,z, modifier = Modifier)
        SimpleGame(modifier = Modifier)
    }
}

