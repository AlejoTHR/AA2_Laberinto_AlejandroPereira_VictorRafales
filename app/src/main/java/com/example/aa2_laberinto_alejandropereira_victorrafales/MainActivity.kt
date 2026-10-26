package com.example.aa2_laberinto_alejandropereira_victorrafales

import android.R.bool
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aa2_laberinto_alejandropereira_victorrafales.ui.theme.AA2_Laberinto_AlejandroPereira_VictorRafalesTheme
import kotlinx.coroutines.delay
import java.util.Vector
import kotlin.math.abs
import kotlin.math.pow


// SENSOR VARIAVLES
private var x by mutableFloatStateOf(0f)
private var y by mutableFloatStateOf(0f)
private var z by mutableFloatStateOf(0f)

private lateinit var sensorManager: SensorManager


// PARA LOS RECTANGULOS
data class Rect(
    var width: Int = 0,
    var height: Int = 0,
    var OffsetX: Int = 0,
    var OffsetY: Int = 0
)

data class circle(
    var OffsetX: Int = 0,
    var OffsetY: Int = 0,
    var Radius: Int = 0
)

var Player = circle(200,400,15)
var Rectangle1 = Rect(100,100,100,100)

// CANVAS VARIABLES
private var MAX_X_SPEED = 8f
private var MAX_Y_SPEED = 8f


class MainActivity : ComponentActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SENSOR INITIALIZATION
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val Accsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(Accsensor != null){
            sensorManager.registerListener(this, Accsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        enableEdgeToEdge()
        setContent {
            AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    BallGamLogic(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onSensorChanged(event : SensorEvent) {
        x = event.values[0]
        y = event.values[1]
        z = event.values[2] // DONT NEED IT

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
fun BallGamLogic(modifier: Modifier) {

    // CIRCLE VARIABLES
    var circleX by remember { mutableFloatStateOf(200f) }
    var circleY by remember { mutableFloatStateOf(400f) }
    var speedX by remember { mutableFloatStateOf(1.5f) }
    var speedY by remember { mutableFloatStateOf(1.5f) }
    var circleRadius by remember { mutableFloatStateOf(15f) }
    var canMove by remember { mutableStateOf(true) }

    var RectX by remember { mutableFloatStateOf(100f) }
    var RectY by remember { mutableFloatStateOf(100f) }
    var RectWitdh by remember { mutableFloatStateOf(100f) }
    var RectHeight by remember { mutableFloatStateOf(100f) }







    LaunchedEffect(Unit) {
        while (true) {
//region NOT DIAGONALS
//            speedX = 0f
//            speedY = 0f
//
//
//            if (x >= 5 && !movingYaxis) {
//                movingXaxis = true
//                speedX = -5f
//
//            } else {
//                movingXaxis = false
//            }
//
//            if (x <= -5 && !movingYaxis) {
//                movingXaxis = true
//                speedX = 5f
//            } else {
//                movingXaxis = false
//            }
//
//            if (y >= 5 && !movingXaxis) {
//                movingYaxis = true
//                speedY = 5f
//            } else {
//                movingYaxis = false
//            }
//            if (y <= -5 && !movingXaxis) {
//                movingYaxis = true
//                speedY = -5f
//            } else {
//                movingYaxis = false
//            }
//
//
//            circleX += speedX
//            circleY += speedY
//endregion

            delay(16)

            // BALL LOGIC
            circleX -= x * speedX
            circleY += y * speedY

            // BALL COLLISION


            // BALL MOVEMENT
            if(canMove) {
                if (x <= -MAX_X_SPEED) {
                    x = -MAX_X_SPEED
                }
                if (x >= MAX_X_SPEED) {
                    x = MAX_X_SPEED
                }
                if (y <= -MAX_Y_SPEED) {
                    y = -MAX_Y_SPEED
                }
                if (y >= MAX_Y_SPEED) {
                    y = MAX_Y_SPEED
                }
            }




        }
    }


        Canvas(modifier = Modifier.fillMaxSize()) {
            // MAIN CIRCLE - PLAYER
            drawCircle(
                color = Color.Red, radius = Player.Radius.dp.toPx(),
                center = Offset(Player.OffsetX.dp.toPx(), Player.OffsetY.dp.toPx())
            )


            drawRect(
                color = Color.Magenta,
                size = Size(Rectangle1.OffsetX.dp.toPx(), Rectangle1.OffsetY.dp.toPx()),
                topLeft = Offset(Rectangle1.width.dp.toPx(),Rectangle1.height.dp.toPx())
            )


        }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(x,y,z, modifier = Modifier)
        BallGamLogic(modifier = Modifier)
    }
}

