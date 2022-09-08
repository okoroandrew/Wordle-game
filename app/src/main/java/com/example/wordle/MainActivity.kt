package com.example.wordle

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var wordToGuess : String
    private var countRound = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordToGuess = FourLetterWordList.getRandomFourLetterWord()

        // find all views by their id
        val wordEditTextView = findViewById<EditText>(R.id.enter_word_editTextText)
        val guessButton = findViewById<Button>(R.id.guess_button)
        val correctWordTextView = findViewById<TextView>(R.id.correct_word_textView)
        val yourGuess1 = findViewById<TextView>(R.id.guess_1_word_textView)
        val guess1Check = findViewById<TextView>(R.id.guess_1_check_word_textView)
        val yourGuess2 = findViewById<TextView>(R.id.guess_2_word_textView)
        val guess2Check = findViewById<TextView>(R.id.guess_2_check_word_textView)
        val yourGuess3 = findViewById<TextView>(R.id.guess_3_word_textView)
        val guess3Check = findViewById<TextView>(R.id.guess_3_check_word_textView)
        val restartButton = findViewById<Button>(R.id.restart_button)
        //val just = findViewById<TextView>(R.id.guess_1_textView)

        // when you click the guess button
        guessButton.setOnClickListener{
            // get the content of the editTextView, make uppercase, clear editText and Hide keyboard
            val editTextContent = wordEditTextView.text.toString().uppercase()
            it.hideKeyboard()
            wordEditTextView.getText().clear()

            if (editTextContent.length == 4 && editTextContent.matches("^[A-Z]*$".toRegex())) {
                when (countRound) {
                    1 -> {
                        yourGuess1.text = editTextContent
//                        just.text = wordToGuess
                        guess1Check.text = checkGuess(editTextContent)
                        if (checkWin(guess1Check.text.toString(), correctWordTextView, countRound)){
                            guessButton.visibility = View.INVISIBLE
                            restartButton.visibility = View.VISIBLE
                        }
                        countRound++
                    }
                    2 -> {
                        yourGuess2.text = editTextContent
                        guess2Check.text = checkGuess(editTextContent)
                        if (checkWin(guess2Check.text.toString(), correctWordTextView, countRound)){
                            guessButton.visibility = View.INVISIBLE
                            restartButton.visibility = View.VISIBLE
                        }
                        countRound++
                    }
                    3 -> {
                        yourGuess3.text = editTextContent
                        guess3Check.text = checkGuess(editTextContent)
                        checkWin(guess3Check.text.toString(), correctWordTextView, countRound)
                        guessButton.visibility = View.INVISIBLE
                        restartButton.visibility = View.VISIBLE
                        countRound++
                    }
                    else -> {
                        //pass
                    }
                }
            }

            else if (editTextContent == "" || editTextContent.length != 4){
                Toast.makeText(it.context, "Enter a four letter word", Toast.LENGTH_SHORT).show()
            }

            else {
                Toast.makeText(it.context, "Invalid input. Guess must be an alphabet", Toast.LENGTH_SHORT).show()
            }

        }

        //Set onClickListener for restart button to restart the game
        restartButton.setOnClickListener {
            guessButton.visibility = View.VISIBLE
            restartButton.visibility = View.INVISIBLE
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            correctWordTextView.visibility = View.INVISIBLE
            yourGuess1.setText(getText(R.string.four_dash))
            yourGuess2.setText(getText(R.string.four_dash))
            yourGuess3.setText(getText(R.string.four_dash))
            guess1Check.setText(resources.getString(R.string.four_dash))
            guess2Check.setText(resources.getString(R.string.four_dash))
            guess3Check.setText(resources.getString(R.string.four_dash))
            countRound = 1
        }

    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }


    private fun checkWin(result: String, correctWordTextView: TextView, round: Int) : Boolean {
        if (result == "OOOO"){
            correctWordTextView.text = wordToGuess
            correctWordTextView.visibility = View.VISIBLE
            correctWordTextView.setTextColor(Color.GREEN)
            Toast.makeText(this, "You Won!!!", Toast.LENGTH_LONG).show()
            return true
        }

        else if(result != "OOOO" && round == 3){
            correctWordTextView.text = wordToGuess
            correctWordTextView.visibility = View.VISIBLE
            correctWordTextView.setTextColor(Color.RED)
            Toast.makeText(this, "You Lost!!!", Toast.LENGTH_LONG).show()
        }
        return false
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}