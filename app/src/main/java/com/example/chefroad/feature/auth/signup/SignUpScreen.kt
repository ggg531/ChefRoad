package com.example.chefroad.feature.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chefroad.R
import com.example.chefroad.ui.theme.Purple1
import com.example.chefroad.ui.theme.Purple2
import com.example.chefroad.ui.theme.Purple3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirm by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value) {
            is SignUpState.Success -> {
                navController.navigate("signin") {
                    popUpTo("signin") { inclusive = true }
                    popUpTo("signup") { inclusive = true }
                }
            }
            is SignUpState.Error -> {
                Toast.makeText(context, context.getString(R.string.signupfailed), Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = null,
                modifier = Modifier.size(400.dp).padding(top = 80.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "이메일") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple1,
                ),
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "비밀번호") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple1,
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                value = confirm,
                onValueChange = { confirm = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "비밀번호 확인") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple1,
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = password.isNotEmpty() && confirm.isNotEmpty() && confirm != password,
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (uiState.value == SignUpState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.signUp(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple2,
                        contentColor = Color.White,
                    ),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && confirm == password,
                ) {
                    Text(text = stringResource(id = R.string.signup))
                }
                TextButton(onClick = { navController.navigate("signin") }) {
                    Text(
                        text = stringResource(id = R.string.signin),
                        color = Purple3,
                    )
                }
            }
        }
    }
}