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
import kotlin.math.abs
import kotlin.math.pow


// SENSOR VARIAVLES
private var x by mutableFloatStateOf(0f)
private var y by mutableFloatStateOf(0f)
private var z by mutableFloatStateOf(0f)

private lateinit var sensorManager: SensorManager


// PARA LOS RECTANGULOS
data class Rect(
    var width: Float = 0f,
    var height: Float = 0f,
    var posX: Float = 0f,
    var posY: Float = 0f
)

data class Circle(
    var posX: Float = 0f,
    var posY: Float = 0f,
    var radius: Float = 0f
)

var Player = Circle(200f,400f,15f)

// RECTANGLES FOR MAZE
var Rectangle1 = Rect(100f,100f,100f,100f)


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
                    SensorScreen(x,y,z, modifier = Modifier)
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

fun intersects (circle : Circle , rect : Rect) : Boolean{

    var circleDistance_X = abs(circle.posX - rect.posX);
    var circleDistance_Y = abs(circle.posY - rect.posY);

    if (circleDistance_X > (rect.width/2 + circle.radius)) { return false; }
    if (circleDistance_Y > (rect.height/2 + circle.radius)) { return false; }

    if (circleDistance_X <= (rect.width/2)) { return true; }
    if (circleDistance_Y <= (rect.height/2)) { return true; }

    var cornerDistance_sq = (circleDistance_X - rect.width/2).pow(2) +
    (circleDistance_Y - rect.height/2).pow(2);

    return (cornerDistance_sq <= (circle.radius.pow(2)));
}

@Composable
fun BallGamLogic(modifier: Modifier) {

    var circleX by remember { mutableFloatStateOf(1.5f) }
    var circleY by remember { mutableFloatStateOf(1.5f) }

    // CIRCLE VARIABLES
    var speedX by remember { mutableFloatStateOf(1.5f) }
    var speedY by remember { mutableFloatStateOf(1.5f) }

    LaunchedEffect(Unit) {
        while (true) {

            delay(16)

            // BALL LOGIC

            Player.posX -= x * speedX
            Player.posY += y * speedY

            circleX -= x * speedX
            circleY += y * speedY


        }
    }

        Canvas(modifier = Modifier.fillMaxSize()) {
            // MAIN CIRCLE - PLAYER
            drawCircle(
                color = Color.Red, radius = Player.radius.dp.toPx(),
                center = Offset(circleX.dp.toPx(), circleY.dp.toPx())
            )


            drawRect(
                color = Color.Magenta,
                size = Size(Rectangle1.posX.dp.toPx(), Rectangle1.posY.dp.toPx()),
                topLeft = Offset(Rectangle1.width.dp.toPx(),Rectangle1.height.dp.toPx())
            )


        }
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

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(x,y,z, modifier = Modifier)
        BallGamLogic(modifier = Modifier)
    }
}

