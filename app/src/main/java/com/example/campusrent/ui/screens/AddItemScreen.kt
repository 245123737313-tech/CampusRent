package com.example.campusrent.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.campusrent.viewmodel.AddItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavController, viewModel: AddItemViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val uploadStatus by viewModel.uploadStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(uploadStatus) {
        uploadStatus?.let { result ->
            if (result.isSuccess) {
                Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show()
                viewModel.resetStatus()
                navController.popBackStack()
            } else {
                Toast.makeText(context, result.exceptionOrNull()?.message ?: "Error adding item", Toast.LENGTH_SHORT).show()
                viewModel.resetStatus()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                 if (imageUri != null) {
                     AsyncImage(
                         model = imageUri,
                         contentDescription = "Selected Image",
                         modifier = Modifier.fillMaxSize(),
                         contentScale = ContentScale.Crop
                     )
                 }
                 
                 Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.align(Alignment.BottomCenter)) {
                     Text(if (imageUri == null) "Select Image" else "Change Image")
                 }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.addItem(name, price, description, imageUri) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Add Item")
                }
            }
        }
    }
}
