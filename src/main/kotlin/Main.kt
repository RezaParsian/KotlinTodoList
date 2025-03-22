package rp.task

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

val header = "================================="
val file = "./data.json"
var tasks: ArrayList<Task> = ArrayList()

@Serializable
data class Task(
    var task: String,
    var isCompleted: Boolean,
)

fun main() {
    val data = File(file);

    if (data.exists())
        tasks = Json.decodeFromString<ArrayList<Task>>(data.readText())

    menu()
}

fun menu() {
    clearConsole()

    val items = arrayOf(
        "Add a Task",
        "View All Tasks",
        "Edit a Task",
        "Mark Task as Completed",
        "Delete a Task",
        "Exit"
    )

    println("$header\n\t\tTO-DO LIST APP\n$header")

    items.mapIndexed { index, item -> println("${index + 1}: $item") }

    readMenu()
}

fun readMenu() {
    val input = readln()

    try {
        when (input.toInt()) {
            1 -> addTask()

            2 -> {
                showAllTasks()
                readln()
            }

            3 -> editTask()

            4 -> markTaskAsComplete()

            5 -> deleteTask()

            6 -> {
                File(file).writeText(Json.encodeToString(tasks))
                exitProcess(0)
            }

            else ->
                println("What are you doing?")
        }


    } catch (e: NumberFormatException) {
        addTask(input)
    }

    menu()
}

fun editTask() {
    showAllTasks()

    println("Chose a task: ")

    val index = readln().toInt() - 1

    println("Changing task (${tasks[index].task}): ")

    tasks[index].task = readln()

    println("Your task edit successfully.")
}

fun deleteTask() {
    showAllTasks()

    println("Chose a task: ")

    val index = readln().toInt() - 1

    println("Are you sure you want to delete this task? (${tasks[index].task})")
    println("if you want to delete this task type yes")

    if (readln() != "yes")
        return

    tasks.removeAt(index)

    println("Your task deleted successfully")

    readln()
}

fun markTaskAsComplete() {
    showAllTasks()

    println("Chose a task: ")

    val index = readln().toInt() -1

    tasks[index].isCompleted = !tasks[index].isCompleted

    println("Your task has marked as ${if (tasks[index].isCompleted) "Finish" else "Unfinished"}!")

    readln()
}

fun showAllTasks() {
    if (tasks.size == 0) {
        println("Add some tasks before")
        readln()
        return
    }

    val isComplete: (Boolean) -> String = { i -> if (i) "[x]" else "[ ]" }

    tasks.forEachIndexed { index, task -> println("${index + 1}:${isComplete(task.isCompleted)} ${task.task}") }
}

fun addTask(task: String) {
    tasks.add(
        Task(
            task,
            false
        )
    )

    println("Your task added successfully")
}

fun addTask() {
    println("Enter your task: ")

    addTask(readln())

    readln()
}

fun clearConsole() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        } else {
            ProcessBuilder("clear").inheritIO().start().waitFor()
        }
    } catch (e: Exception) {
        println("Could not clear console: ${e.message}")
    }
}
