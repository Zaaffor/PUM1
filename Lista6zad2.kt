package com.example.l6z2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

data class Task(
    val id: Int,
    val description: String,
    val maxPoints: Int
)

data class AssignmentList(
    val id: String,
    val subject: String,
    val listNumber: Int,
    val grade: Double,
    val tasks: List<Task>
)

val sampleAssignmentLists = listOf(
    AssignmentList(
        "PUM1_L1",
        "Programowanie Urządzeń Mobilnych 1",
        1,
        4.5,
        listOf(
            Task(1, "Implementacja FizzBuzz", 3),
            Task(2, "Sprawdzenie palindromu", 3),
            Task(3, "Trójkąt Pascala", 4)
        )
    ),
    AssignmentList(
        "PUM1_L2",
        "Programowanie Urządzeń Mobilnych 1",
        2,
        5.0,
        listOf(
            Task(1, "Funkcje rozszerzające", 4),
            Task(2, "Funkcje wyższego rzędu", 6)
        )
    ),
    AssignmentList(
        "SO_L1",
        "Systemy Operacyjne",
        1,
        3.5,
        listOf(
            Task(1, "Implementacja semafora", 5),
            Task(2, "Problem producenta-konsumenta", 5)
        )
    ),
    AssignmentList(
        "SO_L2",
        "Systemy Operacyjne",
        2,
        4.0,
        listOf(
            Task(1, "Algorytmy szeregowania CPU", 6),
            Task(2, "Zarządzanie pamięcią", 4)
        )
    )
)
object Screen {
    const val LISTS = "lists"
    const val GRADES = "grades"
    const val DETAILS = "details/{listId}"

    fun details(listId: String) = "details/$listId"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.LISTS) },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Listy") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.GRADES) },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Oceny") }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.LISTS,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.LISTS) {
                ALScreen(navController)
            }

            composable(Screen.GRADES) {
                GradesScreen()
            }

            composable(
                route = Screen.DETAILS,
                arguments = listOf(
                    navArgument("listId") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val listId = backStackEntry.arguments?.getString("listId")!!
                ListDetailScreen(listId)
            }
        }
    }
}
@Composable
fun ALScreen(navController: NavController) {
    LazyColumn {
        items(sampleAssignmentLists) { list ->

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.details(list.id))
                    }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(list.subject, fontWeight = FontWeight.Bold)
                    Text("Lista ${list.listNumber}")
                    Text("Ocena: ${list.grade}")
                    Text("Liczba zadań: ${list.tasks.size}")
                }
            }
        }
    }
}

@Composable
fun GradesScreen() {

    val averages = sampleAssignmentLists
        .groupBy { it.subject }
        .mapValues { entry ->
            entry.value.map { it.grade }.average()
        }

    LazyColumn {
        averages.forEach { (subject, avg) ->
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(subject, fontWeight = FontWeight.Bold)
                        Text("Średnia ocena: %.2f".format(avg))
                    }
                }
            }
        }
    }
}

@Composable
fun ListDetailScreen(listId: String) {

    val assignment = sampleAssignmentLists.first { it.id == listId }

    LazyColumn {
        item {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "${assignment.subject} – Lista ${assignment.listNumber}",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(assignment.tasks) { task ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Zadanie ${task.id}")
                    Text(task.description)
                    Text("Maks. punktów: ${task.maxPoints}")
                }
            }
        }
    }
}