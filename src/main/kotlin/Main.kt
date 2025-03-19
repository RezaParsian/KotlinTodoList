package rp.task

import java.io.File
import kotlin.system.exitProcess
import kotlinx.serialization.Serializable

val header = "================================="
val file="./data.json"
val tasks: ArrayList<Task> = ArrayList()

@Serializable
data class Task(
    val task: String,
    var isCompleted: Boolean,
)

fun main() {
    menu()
}

fun menu() {
    clearConsole()

    val items = arrayOf(
        "Add a Task",
        "View All Tasks",
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
            1 ->
                addTask()

            2 -> {
                showAllTasks()
                readln()
            }

            3 ->
                markTaskAsComplete()

            4 ->
                deleteTask()

            5 -> {
                File(file).writeText(tasks.joinToString("\n") { "${it.task},${it.isCompleted}" })
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

fun deleteTask() {
    showAllTasks()

    println("Chose a task: ")

    val index = readln().toInt()

    println("Are you sure you want to delete this task? (${tasks[index - 1].task})")
    println("if you want to delete this task type yes")

    if (readln() != "yes")
        return

    tasks.removeAt(index - 1)

    println("Your task deleted successfully")

    readln()
}

fun markTaskAsComplete() {
    showAllTasks()

    println("Chose a task: ")

    val index = readln().toInt()

    tasks[index-1].isCompleted=!tasks[index-1].isCompleted

    println("Your task has marked as ${if (tasks[index-1].isCompleted) "Finish" else "Unfinished"}!")

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
