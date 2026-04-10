package com.internshipuncle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.internshipuncle.core.design.InternshipUncleTheme
import com.internshipuncle.navigation.InternshipUncleApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InternshipUncleRoot()
        }
    }
}

@Composable
private fun InternshipUncleRoot() {
    InternshipUncleTheme {
        InternshipUncleApp()
    }
}

@Preview(showBackground = true)
@Composable
private fun InternshipUnclePreview() {
    InternshipUncleRoot()
}
