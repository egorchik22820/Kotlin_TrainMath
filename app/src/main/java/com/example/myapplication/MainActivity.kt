package com.example.myapplication



import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var correctButton: Button
    private lateinit var wrongButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var statisticsTextView: TextView

    private var correctAnswer: Double = 0.0
    private var totalQuestions: Int = 0
    private var correctAnswers: Int = 0
    private var totalTime: Long = 0

    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        correctButton = findViewById(R.id.correctButton)
        wrongButton = findViewById(R.id.wrongButton)
        resultTextView = findViewById(R.id.resultTextView)
        statisticsTextView = findViewById(R.id.statisticsTextView)

        correctButton.setOnClickListener { checkAnswer(true) }
        wrongButton.setOnClickListener { checkAnswer(false) }

        generateQuestion()
    }

    private fun generateQuestion() {
        val operand1 = Random.nextInt(10, 100)
        val operand2 = Random.nextInt(10, 100)
        val operator = Random.nextInt(4)

        correctAnswer = when (operator) {
            0 -> operand1 * operand2.toDouble() // Multiplication
            1 -> operand1 / operand2.toDouble() // Division
            2 -> operand1 - operand2.toDouble() // Subtraction
            else -> operand1 + operand2.toDouble() // Addition
        }

        // Randomly determine if the answer should be correct or incorrect
        if (Random.nextBoolean()) {
            // Show correct answer
            questionTextView.text = "$operand1 ${getOperatorSymbol(operator)} $operand2 = ${formatAnswer(correctAnswer)}"
        } else {
            // Show incorrect answer
            val incorrectAnswer = correctAnswer + Random.nextInt(1, 10) // Simple way to generate wrong answer
            questionTextView.text = "$operand1 ${getOperatorSymbol(operator)} $operand2 = ${formatAnswer(incorrectAnswer.toDouble())}"
        }

        // Enable buttons and start timing
        correctButton.isEnabled = true
        correctButton.isEnabled = true
        wrongButton.isEnabled = true
        startTime = SystemClock.elapsedRealtime()
    }

    private fun getOperatorSymbol(operator: Int): String {
        return when (operator) {
            0 -> "*"
            1 -> "/"
            2 -> "-"
            else -> "+"
        }
    }

    private fun formatAnswer(answer: Double): String {
        return if (answer % 1.0 == 0.0) {
            answer.toInt().toString()
        } else {
            String.format("%.2f", answer)
        }
    }

    private fun checkAnswer(isCorrect: Boolean) {
        val endTime = SystemClock.elapsedRealtime()
        val timeTaken = endTime - startTime

        totalQuestions++
        if (isCorrect == (correctAnswer == correctAnswer.toInt().toDouble())) {
            correctAnswers++
            resultTextView.text = "ПРАВИЛЬНО"
        } else {
            resultTextView.text = "НЕ ПРАВИЛЬНО"
        }

        totalTime += timeTaken

        updateStatistics()
        correctButton.isEnabled = false
        wrongButton.isEnabled = false

        // Generate new question after a short delay
        questionTextView.postDelayed({ generateQuestion() }, 2000)
    }

    private fun updateStatistics() {
        val correctPercentage = if (totalQuestions > 0) {
            (correctAnswers.toDouble() / totalQuestions) * 100
        } else {
            0.0
        }

        val averageTime = if (totalQuestions > 0) {
            totalTime / totalQuestions
        } else {
            0
        }

        statisticsTextView.text = "Правильные: $correctAnswers/$totalQuestions\n" +
                "Процент: ${String.format("%.2f", correctPercentage)}%\n" +
                "Среднее время: ${averageTime / 1000} сек"
    }
}

