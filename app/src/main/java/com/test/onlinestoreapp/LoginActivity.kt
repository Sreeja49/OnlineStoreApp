package com.test.onlinestoreapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (Utils.GetData(this,"uid")!="") {
                startActivity(Intent(this, ActivityProducts::class.java))
                finish()

            }
            OnlineStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FirebaseApp.initializeApp(this)
                    OnlineStoreLoginScreen()
                }
            }
        }
    }
}

@Composable
fun OnlineStoreLoginScreen() {

    val context = LocalContext.current
    var _email by remember { mutableStateOf("") }
    var _password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {

        Image(painter = painterResource(id = R.drawable.online_store_logo), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(150.dp))

        OutlinedTextField(value = _email, onValueChange = { _email = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        OutlinedTextField(value = _password, onValueChange = { _password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Button(onClick = { handleLogin(context, _email, _password) }, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) { Text("Login") }

       Text(text = "New user? Click to sign up", color = Color.Gray, modifier = Modifier.clickable { navigateToSignUpScreen(context) }.padding(8.dp))
    }
}
private fun navigateToSignUpScreen(context: Context) {
    val intent = Intent(context, SignUpActivity::class.java)
    context.startActivity(intent)
}

private fun handleLogin(context: Context, email: String, password: String) {

    if (email.isEmpty() || password.isEmpty()) {

        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()

    }
    else{

        Utils.showProgressDialog(context)
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {


                        Utils.SetData(context,"uid",user.uid)


                    }
                    Toast.makeText(context, "Login successfully!", Toast.LENGTH_SHORT).show()
                    Utils.hideProgressDialog()
                    val intent = Intent(context, ActivityProducts::class.java)
                    context.startActivity(intent)


                } else {
                    Utils.hideProgressDialog()
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }


    }

}




