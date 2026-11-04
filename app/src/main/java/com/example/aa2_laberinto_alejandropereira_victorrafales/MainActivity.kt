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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aa2_laberinto_alejandropereira_victorrafales.ui.theme.AA2_Laberinto_AlejandroPereira_VictorRafalesTheme
import kotlinx.coroutines.delay
import kotlin.math.abs


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
private var test_text by mutableStateOf("")



class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    var player by mutableStateOf(Circle(70f,250f,10f))
    val initPositionX = player.posX
    val initPositionY = player.posY




    // SENSOR MANAGER
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SENSOR INITIALIZATION
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(accsensor != null){
            sensorManager.registerListener(this, accsensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        enableEdgeToEdge()
        setContent {

            
            AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController, "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            TitleScreen(onBtnClick = { navController.navigate("secondary")})

                        }
                        composable("secondary") {

                            GameScreen(Modifier, { }, {navController.navigate("LoseScreen")},

                                // BALL GAME LOGIC fun
                                player, initPositionX, initPositionY, WinCondition = { navController.navigate("WinScreen")})

                        }
                        composable("WinScreen") {
                            WinScreen({navController.popBackStack()}, { goHome(navController) })
                        }
                        composable("LoseScreen") {
                            LoseScreen({navController.popBackStack()}, { goHome(navController) })
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
    Column(Modifier.fillMaxSize()) {

        Box(Modifier.fillMaxSize().weight(0.7f), contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(R.drawable.ballmaze),
                contentDescription = "Logo",
            )
        }
        Box(Modifier.fillMaxSize().weight(0.2f), contentAlignment = Alignment.Center){
            Text(
                text = "Ball Maze App",
                fontSize = 36.sp,
            )
        }
        Box(Modifier.fillMaxSize().weight(0.1f), contentAlignment = Alignment.Center) {
            Button(onClick = onBtnClick, Modifier.width(200.dp).height(150.dp))
            {
                Text(
                    text = "PLAY",
                    fontSize = 36.sp,
                )
            }
        }

    }


}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onPauseClick: () -> Unit,
    onTimeFinished: () -> Unit,
    player: Circle, initPositionX: Float, initPositionY: Float, WinCondition: () -> Unit

) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        Button(onPauseClick) {
            Text("||")
        }
    }
    ShowTime()

    if(ShowTime()){
        onTimeFinished()
    }

    BallGamLogic(player, Modifier, initPositionX, initPositionY, WinCondition)
}

// GO HOME FROM WINSCREEN
fun goHome(navController: NavController){
    navController.navigate("home"){
        popUpTo("home") {
            inclusive = false
        }
        launchSingleTop = true }
}
@Composable
fun WinScreen(playAgainBtn:()-> Unit, titleBtn:()->Unit){
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().weight(0.7f), contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(R.drawable.win),
                contentDescription = "Logo",
            )
        }
        Box(Modifier.fillMaxSize().weight(0.2f), contentAlignment = Alignment.Center){
            Text(
                text = "YOU WIN",
                fontSize = 36.sp,
            )
        }
        Box(Modifier.fillMaxSize().weight(0.1f), contentAlignment = Alignment.Center) {
            Button(onClick = playAgainBtn, Modifier.width(200.dp).height(150.dp))
            {
                Text(
                    text = "PLAY AGAIN",
                    fontSize = 26.sp,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxSize().weight(0.1f), contentAlignment = Alignment.Center) {
            Button(onClick = titleBtn, Modifier.width(200.dp).height(150.dp))
            {
                Text(
                    text = "GO BACK",
                    fontSize = 36.sp,
                )
            }
        }

    }

}

@Composable
fun LoseScreen(onPlayAgainClick:()->Unit,onGoBackClick:()->Unit ){
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().weight(0.7f), contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(R.drawable.loose),
                contentDescription = "Logo",
            )
        }
        Box(Modifier.fillMaxSize().weight(0.2f), contentAlignment = Alignment.Center){
            Text(
                text = "TIME OUT",
                fontSize = 36.sp,
            )
        }
        Box(Modifier.fillMaxSize().weight(0.1f), contentAlignment = Alignment.Center) {
            Button(onPlayAgainClick, Modifier.width(200.dp).height(150.dp))
            {
                Text(
                    text = "PLAY AGAIN",
                    fontSize = 26.sp,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxSize().weight(0.1f), contentAlignment = Alignment.Center) {
            Button(onGoBackClick, Modifier.width(200.dp).height(150.dp))
            {
                Text(
                    text = "GO BACK",
                    fontSize = 36.sp,
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TittleScreenPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        TitleScreen { }
    }
}

@Preview(showBackground = true)
@Composable
fun WinScreenPreview() {
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        WinScreen({}, {} )
    }
}

//DEBUG SENSOR
@Composable
fun SensorScreen(x:Float , y: Float, z:Float, modifier: Modifier){
    Column(modifier = Modifier.padding(16.dp)) {
        //TextField(value = x.toString(), onValueChange = {}, label = {Text("X")}, readOnly = true)
        //TextField(value = y.toString(), onValueChange = {}, label = {Text("Y")}, readOnly = true)
        //TextField(value = z.toString(), onValueChange = {}, label = {Text("Z")}, readOnly = true)
        //TextField(value= test_text, onValueChange = {}, label = {}, readOnly = true)
    }
}


// SHOW ELAPSED TIME
@Composable
fun ShowTime() : Boolean{
    // SET COUNT DOWN VARS
    var finished by remember { mutableStateOf(false) }
    // INITIAL TIME
    val seconds = 60
    val minutes = 0
    var elapsedMls by remember {mutableLongStateOf(0L)}
    var elapsedScnds by remember {mutableIntStateOf(seconds)}

    var elapsedMnts by remember {mutableIntStateOf(minutes)}

    val starTime = remember { SystemClock.elapsedRealtime() }
    LaunchedEffect(Unit) {
        while (true){
            delay(100)
            // SUBSTRACT SECONDS
            if(elapsedMls < 99) elapsedMls = SystemClock.elapsedRealtime()-starTime
            else {
                elapsedMls = 0
                elapsedScnds--
            }
            // SUBSTACT MINUTES
            if(elapsedScnds == 0){
                elapsedScnds = 59
                elapsedMnts--
            }
            // WHEN TIMERS OUT
            if(elapsedMnts < 0){
                elapsedMnts = 0
                elapsedScnds = 0
                elapsedMls = 0
                finished = true
                break
            }
        }
    }
    if(elapsedScnds <= 9){
        Text("Time: 0$elapsedMnts : 0$elapsedScnds")
    }
    else if(elapsedScnds >= 10) {
        Text("Time: 0$elapsedMnts : $elapsedScnds")
    }

    return finished
}


// COLLISIONS
fun intersects (circle : Circle , rect : Rect) : Boolean{
    Log.d("Intersect", "Circle [$circle] | Rectangle [$rect]")
    
    /*
    if(circle.posX >= rect.posX && circle.posX <= rect.posX + rect.width &&
        circle.posY >= rect.posY && circle.posY <= rect.posY + rect.height){
        return true
    }*/
    val distX = abs(circle.posX - rect.posX - rect.width / 2)
    val distY = abs(circle.posY - rect.posY-rect.height/2)

    if (distX > (rect.width/2 + circle.radius)) { return false; }
    if (distY > (rect.height/2 + circle.radius)) { return false; }

    if (distX <= (rect.width/2)) { return true; }
    if (distY <= (rect.height/2)) { return true; }

    val dx=distX-rect.width/2
    val dy=distY-rect.height/2
    return (dx*dx+dy*dy<=(circle.radius*circle.radius))
}


// BALL n COLLISIONS
@Composable
fun BallGamLogic(player : Circle, modifier: Modifier, initPositionX : Float, initPositionY : Float, WinCondition : () -> Unit) {
    // CIRCLE VARIABLES
     player.posX = initPositionX
     player.posY = initPositionY

    var speedX by remember { mutableFloatStateOf(1.5f) }
    var speedY by remember { mutableFloatStateOf(1.5f) }

    var gameEnd = false

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        // SCREEN DIMENSIONS
        val screenWidth = maxWidth.value

        // END GAME DIMENSIONS
        val endGame = Rect(40f, 40f, 40f, 140f)

        // MAP DIMENSIONS LIMITS
        val rectangleTop = Rect(screenWidth, endGame.height, 0f, 100f)
        val rectangleDown = Rect(screenWidth, endGame.height, 0f, 500f)
        val rectangleLeft = Rect(endGame.width, rectangleDown.posY + endGame.height - rectangleTop.posY, 0f, rectangleTop.posY)
        val rectangleRight = Rect(endGame.width, rectangleDown.posY + endGame.height - rectangleTop.posY, screenWidth - rectangleLeft.width, rectangleTop.posY)
        // MAP DIMENSIONS INSIDE
        val rectangle1 = Rect(endGame.width*2f, endGame.height, endGame.posX, endGame.posY+endGame.height)
        val rectangle2 = Rect(endGame.width, endGame.height*(3f), endGame.posX+endGame.width*2f, endGame.posY+endGame.height)
        val rectangle3 = Rect(endGame.width, endGame.height, endGame.posX+endGame.width*3f, endGame.posY+endGame.height*2f)
        val rectangle4 = Rect(endGame.width, endGame.height, endGame.posX+endGame.width*4f, endGame.posY)
        val rectangle5 = Rect(screenWidth, endGame.height, endGame.posX+endGame.width*6f, endGame.posY+endGame.height*1f)
        val rectangle6 = Rect(endGame.width, rectangleDown.posY - (endGame.posY+endGame.height*6), endGame.posX+endGame.width*5f, endGame.posY+endGame.height*6)
        val rectangle7 = Rect(endGame.width, endGame.height*3f, endGame.posX+endGame.width*3f, endGame.posY+endGame.height*4)
        val rectangle8 = Rect(endGame.width*5f, endGame.height, endGame.posX+endGame.width, endGame.posY+endGame.height*4)
        val rectangle9 = Rect(screenWidth, endGame.height, endGame.posX+endGame.width*7f, endGame.posY+endGame.height*4)
        val rectangle10 = Rect(rectangle6.posX - (endGame.posX+endGame.width*3f), endGame.height, endGame.posX, rectangleDown.posY-endGame.height*2f)
        val rectangle11 = Rect(endGame.width, endGame.height, endGame.posX+endGame.width*3f, rectangleDown.posY-endGame.height)
        val rectangle12 = Rect(endGame.width, endGame.height, endGame.posX+endGame.width, rectangleDown.posY-endGame.height*3f)
        val rectangle13 = Rect(endGame.width*2f, endGame.height, endGame.posX+endGame.width*5f, endGame.posY+endGame.height*2f)

        LaunchedEffect(Unit) {
            while (!gameEnd) {
                delay(16)

                // DETEECTION END GAME
                if (intersects(player, endGame)) {
                    gameEnd = true
                    delay(300)

                    // Acción: Cambiar de pantalla y pasar puntuación
                    //navegarAPantallaVictoria(player.puntuacion)
                }

                val nextX = -x * speedX
                val nextY = y * speedY

                player.posX += nextX
                player.posY += nextY

                //DETECTION MAP HORIZONTAL
                if (intersects(player, rectangleTop) ||
                    intersects(player, rectangleLeft) ||
                    intersects(player, rectangleRight) ||
                    intersects(player, rectangleDown) ||
                    intersects(player, rectangle1) ||
                    intersects(player, rectangle2) ||
                    intersects(player, rectangle3) ||
                    intersects(player, rectangle4) ||
                    intersects(player, rectangle5) ||
                    intersects(player, rectangle6) ||
                    intersects(player, rectangle7) ||
                    intersects(player, rectangle8) ||
                    intersects(player, rectangle9) ||
                    intersects(player, rectangle10) ||
                    intersects(player, rectangle11) ||
                    intersects(player, rectangle12) ||
                    intersects(player, rectangle13)
                ) {
                    //test_text = "COLLISION " + x.toString() + ", " + y.toString()
                    player.posX -= nextX
                } else {
                    //test_text = "NO COLLISION" + x.toString() + ", " + y.toString()
                }

                //DETECTION MAP VERTICAL
                if (intersects(player, rectangleTop) ||
                    intersects(player, rectangleLeft) ||
                    intersects(player, rectangleRight) ||
                    intersects(player, rectangleDown) ||
                    intersects(player, rectangle1) ||
                    intersects(player, rectangle2) ||
                    intersects(player, rectangle3) ||
                    intersects(player, rectangle4) ||
                    intersects(player, rectangle5) ||
                    intersects(player, rectangle6) ||
                    intersects(player, rectangle7) ||
                    intersects(player, rectangle8) ||
                    intersects(player, rectangle9) ||
                    intersects(player, rectangle10) ||
                    intersects(player, rectangle11) ||
                    intersects(player, rectangle12) ||
                    intersects(player, rectangle13)
                ) {
                    //test_text = "COLLISION " + x.toString() + ", " + y.toString()
                    player.posY -= nextY

                } else {
                    //test_text = "NO COLLISION" + x.toString() + ", " + y.toString()
                }


                // DETECTION FOR WIN RECT
                if(intersects(player, endGame)){
                    WinCondition()
                }


                // BALL LOGIC
//                player.posX -= x * speedX
//                player.posY += y * speedY
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            // MAIN CIRCLE - PLAYER
            drawCircle(
                color = Color.Red, radius = player.radius.dp.toPx(),
                center = Offset(player.posX.dp.toPx(),  player.posY.dp.toPx())
            )
            // RECTANGLE - END GAME
            drawRect(
                color = Color.Magenta,
                size = Size(endGame.width.dp.toPx(), endGame.height.dp.toPx()),
                topLeft = Offset(endGame.posX.dp.toPx(), endGame.posY.dp.toPx())
            )
            // RECTANGLES - MAP LIMITS
            // RECTANGLE TOP
            drawRect(
                color = Color.Gray,
                size = Size(rectangleTop.width.dp.toPx(),rectangleTop.height.dp.toPx()),
                topLeft = Offset(rectangleTop.posX.dp.toPx(),rectangleTop.posY.dp.toPx())
            )
            // RECTANGLE DOWN
            drawRect(
                color = Color.Gray,
                size = Size(rectangleDown.width.dp.toPx(), rectangleDown.height.dp.toPx()),
                topLeft = Offset(rectangleDown.posX.dp.toPx(), rectangleDown.posY.dp.toPx())
            )
            // RECTANGLE LEFT
            drawRect(
                color = Color.Gray,
                size = Size(rectangleLeft.width.dp.toPx(),rectangleLeft.height.dp.toPx()),
                topLeft = Offset(rectangleLeft.posX.dp.toPx(),rectangleLeft.posY.dp.toPx())
            )
            // RECTANGLE RIGHT
            drawRect(
                color = Color.Gray,
                size = Size(rectangleRight.width.dp.toPx(),rectangleRight.height.dp.toPx()),
                topLeft = Offset(rectangleRight.posX.dp.toPx(), rectangleRight.posY.dp.toPx())
            )

            // RECTANGLES - MAP INSIDE
            // RECTANGLE 1
            drawRect(
                color = Color.Gray,
                size = Size(rectangle1.width.dp.toPx(),rectangle1.height.dp.toPx()),
                topLeft = Offset(rectangle1.posX.dp.toPx(),rectangle1.posY.dp.toPx())
            )
            // RECTANGLE 2
            drawRect(
                color = Color.Gray,
                size = Size(rectangle2.width.dp.toPx(), rectangle2.height.dp.toPx()),
                topLeft = Offset(rectangle2.posX.dp.toPx(), rectangle2.posY.dp.toPx())
            )
            // RECTANGLE 3
            drawRect(
                color = Color.Gray,
                size = Size(rectangle3.width.dp.toPx(),rectangle3.height.dp.toPx()),
                topLeft = Offset(rectangle3.posX.dp.toPx(),rectangle3.posY.dp.toPx())
            )
            // RECTANGLE 4
            drawRect(
                color = Color.Gray,
                size = Size(rectangle4.width.dp.toPx(),rectangle4.height.dp.toPx()),
                topLeft = Offset(rectangle4.posX.dp.toPx(), rectangle4.posY.dp.toPx())
            )
            // RECTANGLE 5
            drawRect(
                color = Color.Gray,
                size = Size(rectangle5.width.dp.toPx(),rectangle5.height.dp.toPx()),
                topLeft = Offset(rectangle5.posX.dp.toPx(), rectangle5.posY.dp.toPx())
            )
            // RECTANGLE 6
            drawRect(
                color = Color.Gray,
                size = Size(rectangle6.width.dp.toPx(),rectangle6.height.dp.toPx()),
                topLeft = Offset(rectangle6.posX.dp.toPx(), rectangle6.posY.dp.toPx())
            )
            // RECTANGLE 7
            drawRect(
                color = Color.Gray,
                size = Size(rectangle7.width.dp.toPx(),rectangle7.height.dp.toPx()),
                topLeft = Offset(rectangle7.posX.dp.toPx(), rectangle7.posY.dp.toPx())
            )
            // RECTANGLE 8
            drawRect(
                color = Color.Gray,
                size = Size(rectangle8.width.dp.toPx(),rectangle8.height.dp.toPx()),
                topLeft = Offset(rectangle8.posX.dp.toPx(), rectangle8.posY.dp.toPx())
            )
            // RECTANGLE 9
            drawRect(
                color = Color.Gray,
                size = Size(rectangle9.width.dp.toPx(),rectangle9.height.dp.toPx()),
                topLeft = Offset(rectangle9.posX.dp.toPx(), rectangle9.posY.dp.toPx())
            )
            // RECTANGLE 10
            drawRect(
                color = Color.Gray,
                size = Size(rectangle10.width.dp.toPx(),rectangle10.height.dp.toPx()),
                topLeft = Offset(rectangle10.posX.dp.toPx(), rectangle10.posY.dp.toPx())
            )
            // RECTANGLE 11
            drawRect(
                color = Color.Gray,
                size = Size(rectangle11.width.dp.toPx(),rectangle11.height.dp.toPx()),
                topLeft = Offset(rectangle11.posX.dp.toPx(), rectangle11.posY.dp.toPx())
            )
            // RECTANGLE 12
            drawRect(
                color = Color.Gray,
                size = Size(rectangle12.width.dp.toPx(),rectangle12.height.dp.toPx()),
                topLeft = Offset(rectangle12.posX.dp.toPx(), rectangle12.posY.dp.toPx())
            )
            // RECTANGLE 13
            drawRect(
                color = Color.Gray,
                size = Size(rectangle13.width.dp.toPx(),rectangle13.height.dp.toPx()),
                topLeft = Offset(rectangle13.posX.dp.toPx(), rectangle13.posY.dp.toPx())
            )
        }
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
    val player = Circle(100f,100f,15f)
    AA2_Laberinto_AlejandroPereira_VictorRafalesTheme {
        SensorScreen(0f,0f,0f, modifier = Modifier)
        BallGamLogic(player, modifier = Modifier, initPositionX = player.posX, initPositionY = player.posY, WinCondition = {})
        TitleScreen { }
    }
}

