package com.example.rma_lv


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.AllPermission
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.rma_lv.ui.theme.RMA_LVTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val navController = rememberNavController()
            val sensorManager = (LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
            val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            RMA_LVTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    BackgroundImage(modifier = Modifier)


                    if(currentUser != null) {
                        NavHost(navController = navController, startDestination = "main_screen") {
                            composable("main_screen") {
                                MainScreen(navController = navController)
                            }
                            composable("step_counter") {
                                //StepCounter(navController = navController)
                                if (sensor != null) {
                                    StepCounter(navController = navController, sensorManager = sensorManager, sensor = sensor)
                                }
                            }
                            composable("login-screen") {
                                LoginRegisterScreen(navController)
                            }
                        }
                    }
                    else {
                        NavHost(navController = navController, startDestination = "login-screen") {
                            composable("main_screen") {
                                MainScreen(navController = navController)
                            }
                            composable("step_counter") {
                                //StepCounter(navController = navController)
                                if (sensor != null) {
                                    StepCounter(navController = navController, sensorManager = sensorManager, sensor = sensor)
                                }
                            }
                            composable("login-screen") {
                                LoginRegisterScreen(navController)
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun UserPrew() {
    val name = "Maja"
    val visina = 1.68f
    val tezina = 68f
    val bmi = tezina /(visina * visina)
    val formattedBmi = String.format("%.2f", bmi)


    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ){
        Text(
            text = "Pozdrav $name!",
            fontSize = 20.sp,
            lineHeight = 56.sp,
            modifier= Modifier.padding(top = 8.dp)
        )

        Text (
            text = "Tvoj BMI je:",
            fontSize = 55.sp,
            lineHeight = 61.sp,
            textAlign = TextAlign.Center,
        )

        Text(
            text = formattedBmi,
            fontSize = 70.sp,
            lineHeight = 72.sp,
            fontWeight = FontWeight.Bold
        )

    }
}


/*
@Composable
fun UserPreview(name: String, visina: Float, tezina: Float, modifier: Modifier = Modifier) {

    val db = FirebaseFirestore.getInstance()
    var bmi by remember { mutableStateOf(0f) }
    var formattedBmi by remember { mutableStateOf(String.format("%.2f", bmi)) }

    Text(
        text = "Pozdrav $name!",
        fontSize = 20.sp,
        lineHeight = 56.sp,
        modifier= Modifier
            .padding(top = 8.dp)
            .padding(start = 10.dp)

    )

    var newTezina by remember { mutableStateOf(0f) }
    var newVisina by remember { mutableStateOf(0f) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Text(
            text = "Tvoj BMI je:",
            fontSize = 55.sp,
            lineHeight = 61.sp,
            textAlign = TextAlign.Center,


            )
        Text(
            text = formattedBmi,
            fontSize = 70.sp,
            lineHeight = 72.sp,
            fontWeight = FontWeight.Bold,
        )

        TextField(
            value = newTezina.toString(),
            onValueChange = { newTezina = it.toFloatOrNull() ?: 0f },
            label = { Text("Nova Tezina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        TextField(
            value = newVisina.toString(),
            onValueChange = { newVisina = it.toFloatOrNull() ?: 0f },
            label = { Text("Nova Visina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )


        val vrijednosti = hashMapOf(
            "Tezina" to newTezina,
            "visina" to newVisina
        )
        Button(onClick = {
            val docRef = db.collection("BMI").document("uv8rOhq2c80gQcccGsex")
            docRef.set(vrijednosti)
                .addOnSuccessListener {
                    // Update successful (optional: show a success message)
                }
                .addOnFailureListener { e ->
                    // Update failed (handle error, e.g., show an error message)
                    Log.e("MainActivity", "Error updating Tezina: $e")
                }
            docRef.get().addOnSuccessListener { it ->
                newTezina = it.getDouble("Tezina")?.toFloat()!!
                newVisina = it.getDouble("visina")?.toFloat()!!
                bmi = newTezina / (newVisina * newVisina)
                formattedBmi=String.format("%.2f", bmi)


            }

            }
        )
        {Text(
            "Unesi")}

        }
}

 */
@Composable
fun BackgroundImage(modifier: Modifier) {

    Box (modifier){ Image(
        painter = painterResource(id = R.drawable.fitnesss),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.1F
    )
    }

}
@Preview(showBackground = false)
@Composable
fun UserPreview() {
    RMA_LVTheme {
        BackgroundImage(modifier = Modifier)   }
}


@Composable
fun MainScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        UserPrew()
        Button(onClick = {
            navController.navigate("step_counter")
        },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        {
            Text(text = "Step Counter")
        }
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("login-screen")
        },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        {
            Text(text = "Log out")
        }
    }
}


@Composable
fun StepCounter(navController: NavController, sensorManager: SensorManager, sensor: Sensor) {
    val db = FirebaseFirestore.getInstance()
    var stepCount by remember {
        mutableStateOf(0)
    }
    val accValue = FloatArray(3)
    var magPrev by remember {
        mutableStateOf(0f)
    }
    var magDelta by remember {
        mutableStateOf(0.0)
    }

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }

        override fun onSensorChanged(sensEvent: SensorEvent) {
            if(sensEvent?.sensor == sensor) {
                sensEvent.values.copyInto(accValue)
                val x_acc = sensEvent.values[0]
                val y_acc = sensEvent.values[1]
                val z_acc = sensEvent.values[2]
                val mag = sqrt(x_acc*x_acc + y_acc*y_acc+ z_acc*z_acc )
                magDelta = (mag-magPrev).toDouble()
                magPrev = mag
                if(magDelta > 13) {
                    stepCount++
                }
            }
        }
    }
    sensorManager.registerListener(sensorEventListener, sensor,SensorManager.SENSOR_DELAY_NORMAL)
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        Column {
            Text (
                text = "Step Count",
                fontSize = 30.sp
            )
            Text (
                text = "$stepCount",
                fontSize = 20.sp
            )
            Button(onClick = {
                val docRef = db.collection("BMI").document("uv8rOhq2c80gQcccGsex")
                docRef.update("koraci", stepCount)
                    .addOnSuccessListener {
                        // Update successful (optional: show a success message)
                    }
                    .addOnFailureListener { e ->
                        // Update failed (handle error, e.g., show an error message)
                        Log.e("MainActivity", "Error updating Tezina: $e")
                    }
            }) {
                Text(text = "Unesi")

            }
        }
        Button(onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("User Info")
        }
    }
}

@Composable
fun LoginRegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { signIn(context, email, password, navController) }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { register(context, email, password, navController) }) {
            Text("Register")
        }
    }
}

private fun signIn(context: Context, email: String, password: String,navController: NavController) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("main_screen")

            } else {
                // Prijavljivanje neuspješno
                Toast.makeText(context, "Login failed",
                    Toast.LENGTH_SHORT).show()
            }
        }
}

private fun register(context: Context, email: String, password: String, navController: NavController) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen")


            } else {
                // Registracija neuspješna
                Toast.makeText(context, "Registration failed",
                    Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { e ->
            Log.e("Registration", "Registration failed", e)
            Toast.makeText(context, "Registration failedd: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

