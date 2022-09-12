package com.example.wordle

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.text.color

class MainActivity : AppCompatActivity() {

    private lateinit var wordToGuess : String
    private var countRound = 0
    private var streak = 0

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
        val nextButton = findViewById<Button>(R.id.next_button)
        val resetButton = findViewById<ImageView>(R.id.reset_button)
        val streakTextView = findViewById<TextView>(R.id.streak_textView)

        guessButton.setOnClickListener{
            // get the content of the editTextView, make uppercase, clear editText and Hide keyboard
            val editTextContent = wordEditTextView.text.toString().uppercase()
            it.hideKeyboard()
            wordEditTextView.text.clear()

            if (editTextContent.length == 4 && editTextContent.matches("^[A-Z]*$".toRegex())) {
                countRound++

                when (countRound) {
                    1 -> {
                        yourGuess1.text = editTextContent
                        guess1Check.text = checkGuess(editTextContent)
                        if (checkWin(guess1Check.text.toString(), correctWordTextView, countRound, streakTextView)){
                            guessButton.visibility = View.INVISIBLE
                            nextButton.visibility = View.VISIBLE
                        }
                    }
                    2 -> {
                        yourGuess2.text = editTextContent
                        guess2Check.text = checkGuess(editTextContent)
                        if (checkWin(guess2Check.text.toString(), correctWordTextView, countRound, streakTextView)){
                            guessButton.visibility = View.INVISIBLE
                            nextButton.visibility = View.VISIBLE
                        }
                    }
                    3 -> {
                        yourGuess3.text = editTextContent
                        guess3Check.text = checkGuess(editTextContent)
                        (checkWin(guess3Check.text.toString(), correctWordTextView, countRound, streakTextView))
                        guessButton.visibility = View.INVISIBLE
                        nextButton.visibility = View.VISIBLE
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
        nextButton.setOnClickListener {
            guessButton.visibility = View.VISIBLE
            nextButton.visibility = View.INVISIBLE
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            correctWordTextView.visibility = View.INVISIBLE
            yourGuess1.text = getText(R.string.four_dash)
            yourGuess2.text = getText(R.string.four_dash)
            yourGuess3.text = getText(R.string.four_dash)
            guess1Check.text = getString(R.string.four_dash)
            guess2Check.text = resources.getString(R.string.four_dash)
            guess3Check.text = resources.getString(R.string.four_dash)
            countRound = 0
        }

        //reset button gives you a new word and clear your gueses
        resetButton.setOnClickListener {
            if (nextButton.visibility == View.INVISIBLE) {
                wordToGuess = FourLetterWordList.getRandomFourLetterWord()
                correctWordTextView.visibility = View.INVISIBLE
                yourGuess1.text = getText(R.string.four_dash)
                yourGuess2.text = getText(R.string.four_dash)
                yourGuess3.text = getText(R.string.four_dash)
                guess1Check.text = resources.getString(R.string.four_dash)
                guess2Check.text = resources.getString(R.string.four_dash)
                guess3Check.text = resources.getString(R.string.four_dash)
                countRound = 0
            }

            else{
                Toast.makeText(it.context, "cannot reset word", Toast.LENGTH_SHORT).show()
            }
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
    private fun checkGuess(guess: String): SpannableStringBuilder {
        val s = SpannableStringBuilder()

        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                s.color(Color.GREEN) { append(guess[i]) }
            }

            else if (guess[i] in wordToGuess) {
                s.color(Color.BLUE) { append(guess[i]) }
            }

            else {
                s.color(Color.RED) { append(guess[i]) }
            }

        }
        return s
    }


    private fun checkWin(result: String, correctWordTextView: TextView, round: Int, streakTextView: TextView) : Boolean {
        if (result.equals(wordToGuess, true)){
            correctWordTextView.text = wordToGuess
            correctWordTextView.visibility = View.VISIBLE
            correctWordTextView.setTextColor(Color.GREEN)
            Toast.makeText(this, "You Won!!!", Toast.LENGTH_LONG).show()
            streak++
            streakTextView.text = streak.toString()
            return true
        }

        else if(!result.equals(wordToGuess, true) && round == 3){
            correctWordTextView.text = wordToGuess
            correctWordTextView.visibility = View.VISIBLE
            correctWordTextView.setTextColor(Color.RED)
            Toast.makeText(this, "You Lost!!!", Toast.LENGTH_LONG).show()
        }
        return false
    }


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}