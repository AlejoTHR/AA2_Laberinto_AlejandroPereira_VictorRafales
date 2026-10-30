package com.example.aa2_laberinto_alejandropereira_victorrafales

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aa2_laberinto_alejandropereira_victorrafales.Circle
import com.example.aa2_laberinto_alejandropereira_victorrafales.ui.theme.AA2_Laberinto_AlejandroPereira_VictorRafalesTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.pow


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



// RECTANGLES FOR MAZE

// SENSOR VARIABLES
private var x by mutableFloatStateOf(0f)
private var y by mutableFloatStateOf(0f)
private var z by mutableFloatStateOf(0f)
private var test_text by mutableStateOf("");


class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    var player by mutableStateOf(Circle(100f,100f,15f))

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
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(x,y,z, modifier = Modifier)
                    BallGamLogic( player, modifier = Modifier.padding(innerPadding))
                    NavHost(
                        navController, "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            TitleScreen(onBtnClick = { navController.navigate("secondary")})
                        }
                        composable("secondary") {
                            GameScreen(onBtnClick = { navController.popBackStack()})
                        }
                    }
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
fun TitleScreen(
    modifier: Modifier = Modifier,
    onBtnClick: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    }
    Button(onClick = onBtnClick)
    {

    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onBtnClick: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    }
    Button(onClick = onBtnClick)
    {

    }
}

@Composable
fun SensorScreen(x:Float , y: Float, z:Float, modifier: Modifier){
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = x.toString(), onValueChange = {}, label = {Text("X")}, readOnly = true)
        TextField(value = y.toString(), onValueChange = {}, label = {Text("Y")}, readOnly = true)
        TextField(value = z.toString(), onValueChange = {}, label = {Text("Z")}, readOnly = true)
        TextField(value= test_text, onValueChange = {}, label = {}, readOnly = true)
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
    Log.d("Intersect", "Circle [$circle] | Rectangle [$rect]")
    
    /*
    if(circle.posX >= rect.posX && circle.posX <= rect.posX + rect.width &&
        circle.posY >= rect.posY && circle.posY <= rect.posY + rect.height){
        return true
    }*/
    val distX = abs(circle.posX - rect.posX - rect.width / 2);
    val distY = abs(circle.posY - rect.posY-rect.height/2);

    if (distX > (rect.width/2 + circle.radius)) { return false; }
    if (distY > (rect.height/2 + circle.radius)) { return false; }

    if (distX <= (rect.width/2)) { return true; }
    if (distY <= (rect.height/2)) { return true; }

    val dx=distX-rect.width/2;
    val dy=distY-rect.height/2;
    return (dx*dx+dy*dy<=(circle.radius*circle.radius));
}

@Composable
fun BallGamLogic(player : Circle, modifier: Modifier) {
    // CIRCLE VARIABLES
    var speedX by remember { mutableFloatStateOf(1.5f) }
    var speedY by remember { mutableFloatStateOf(1.5f) }

    var Rectangle1 = Rect(100f,100f,100f,100f)


    LaunchedEffect(Unit) {
        while (true) {
            delay(16)

            if(intersects(player, Rectangle1)){
                test_text = "COLLISION " + x.toString() + ", " + y.toString()
            }else{
                test_text = "NO COLLISION" + x.toString() + ", " + y.toString()
            }
            // BALL LOGIC
            player.posX -= x * speedX
            player.posY += y * speedY
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        // MAIN CIRCLE - PLAYER
        drawCircle(
            color = Color.Red, radius = player.radius.dp.toPx(),
            center = Offset(player.posX.dp.toPx(), player.posY.dp.toPx())
        )


        drawRect(
            color = Color.Magenta,
            size = Size(Rectangle1.width.dp.toPx(), Rectangle1.height.dp.toPx()),
            topLeft = Offset(Rectangle1.posX.dp.toPx(),Rectangle1.posY.dp.toPx())
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
    var player = Circle(100f,100f,15f);
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(0f,0f,0f, modifier = Modifier)
        BallGamLogic(player, modifier = Modifier)
    }
}

