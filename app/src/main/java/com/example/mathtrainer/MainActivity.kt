package com.example.mathtrainer

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvExample: TextView
    private lateinit var tvStatistics: TextView
    private lateinit var tvPercent: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnStart: Button
    private lateinit var btnCheck: Button

    private var firstNumber = 0
    private var secondNumber = 0
    private var operation = ""
    private var correctAnswer = 0

    private var correctCount = 0
    private var wrongCount = 0

    private var exampleCreated = false
    private var answerChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExample = findViewById(R.id.tvExample)
        tvStatistics = findViewById(R.id.tvStatistics)
        tvPercent = findViewById(R.id.tvPercent)
        etAnswer = findViewById(R.id.etAnswer)
        btnStart = findViewById(R.id.btnStart)
        btnCheck = findViewById(R.id.btnCheck)

        btnStart.setOnClickListener {
            generateExample()
        }

        btnCheck.setOnClickListener {
            checkAnswer()
        }

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            updateStatistics()
            etAnswer.isEnabled = false
            btnCheck.isEnabled = false
        }
    }

    private fun generateExample() {

        operation = listOf("+", "-", "*", "/").random()

        if (operation == "/") {

            do {
                secondNumber = Random.nextInt(10, 100)
                val multiplier = Random.nextInt(1, 10)
                firstNumber = secondNumber * multiplier
            } while (firstNumber > 99)

        } else {
            firstNumber = Random.nextInt(10, 100)
            secondNumber = Random.nextInt(10, 100)
        }

        correctAnswer = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> firstNumber / secondNumber
            else -> 0
        }

        tvExample.text = "$firstNumber $operation $secondNumber ="
        tvExample.setBackgroundColor(Color.WHITE)

        etAnswer.text.clear()

        btnStart.isEnabled = false
        btnCheck.isEnabled = true
        etAnswer.isEnabled = true

        exampleCreated = true
        answerChecked = false
    }

    private fun checkAnswer() {

        val userAnswer = etAnswer.text.toString().toIntOrNull()

        if (userAnswer == correctAnswer) {
            correctCount++
            tvExample.setBackgroundColor(Color.GREEN)
        } else {
            wrongCount++
            tvExample.setBackgroundColor(Color.RED)
        }

        etAnswer.isEnabled = false
        btnCheck.isEnabled = false
        btnStart.isEnabled = true

        answerChecked = true

        updateStatistics()
    }

    private fun updateStatistics() {

        tvStatistics.text =
            "Правильных: $correctCount    Неправильных: $wrongCount"

        val total = correctCount + wrongCount

        val percent = if (total == 0) {
            0.0
        } else {
            correctCount * 100.0 / total
        }

        tvPercent.text =
            String.format(Locale.getDefault(), "%.2f%%", percent)
    }

    private fun restoreState(state: Bundle) {

        firstNumber = state.getInt("firstNumber")
        secondNumber = state.getInt("secondNumber")
        operation = state.getString("operation", "")
        correctAnswer = state.getInt("correctAnswer")

        correctCount = state.getInt("correctCount")
        wrongCount = state.getInt("wrongCount")

        exampleCreated = state.getBoolean("exampleCreated")
        answerChecked = state.getBoolean("answerChecked")

        etAnswer.setText(state.getString("answer", ""))

        updateStatistics()

        if (exampleCreated) {

            tvExample.text = "$firstNumber $operation $secondNumber ="

            btnStart.isEnabled = false

            if (answerChecked) {
                etAnswer.isEnabled = false
                btnCheck.isEnabled = false

                val correct = state.getBoolean("wasCorrect")

                if (correct) {
                    tvExample.setBackgroundColor(Color.GREEN)
                } else {
                    tvExample.setBackgroundColor(Color.RED)
                }

            } else {
                etAnswer.isEnabled = true
                btnCheck.isEnabled = true
                tvExample.setBackgroundColor(Color.WHITE)
            }

        } else {

            btnStart.isEnabled = true
            btnCheck.isEnabled = false
            etAnswer.isEnabled = false
            tvExample.text = "00 + 00 ="
            tvExample.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        outState.putInt("firstNumber", firstNumber)
        outState.putInt("secondNumber", secondNumber)
        outState.putString("operation", operation)
        outState.putInt("correctAnswer", correctAnswer)

        outState.putInt("correctCount", correctCount)
        outState.putInt("wrongCount", wrongCount)

        outState.putBoolean("exampleCreated", exampleCreated)
        outState.putBoolean("answerChecked", answerChecked)

        outState.putString("answer", etAnswer.text.toString())

        val wasCorrect =
            answerChecked &&
                    etAnswer.text.toString().toIntOrNull() == correctAnswer

        outState.putBoolean("wasCorrect", wasCorrect)
    }
}