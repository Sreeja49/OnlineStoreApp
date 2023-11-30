package com.test.onlinestoreapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpScreen()
                }
            }
        }
    }
}

@Composable
fun SignUpScreen() {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp))
    {

        Image(painter = painterResource(id = R.drawable.online_store_logo), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Button(onClick = { handleSignUp(context, name, email, password) }, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) { Text("Sign Up") }
    }
}

private fun handleSignUp(context: Context, name: String, email: String, password: String) {

    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

        Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()

    }
    else{

        Utils.showProgressDialog(context)
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Utils.hideProgressDialog()
                    Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Utils.hideProgressDialog()
                    Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpActivityPreview() {
    OnlineStoreAppTheme {
        SignUpScreen()
    }
}
