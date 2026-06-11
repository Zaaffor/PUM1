package com.example.l6z1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface( modifier = Modifier.fillMaxSize() ) {
                        WebsiteList()
                }
            }
        }
    }
}

@Composable
fun WebsiteList() {
    val context = LocalContext.current

    val websites = listOf(
        "https://www.google.com",
        "https://developer.android.com",
        "https://kotlinlang.org",
        "https://kotlinlang.org/docs/sealed-classes.html#declare-a-sealed-class-or-interface"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
    ) {
        items(websites) { url ->
            Text(
                text = url,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = url.toUri()
                        }
                        context.startActivity(intent)
                    },
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}
