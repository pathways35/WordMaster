package com.example.wordmaster.screens.game

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.*
import com.example.wordmaster.database.Model.Word
import com.example.wordmaster.database.WordDatabaseDao
import com.example.wordmaster.database.WordMasterDatabase
import com.example.wordmaster.repository.WordsRepository
import kotlinx.coroutines.*
import kotlin.random.Random


class GameViewModel(val db:WordDatabaseDao, application: Application): AndroidViewModel(application) {

    companion object {
        //Represents when the game is over
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        //total time of the game
        private const val COUNTDOWN_TIME = 60000L

    }
    //timer
    private val timer: CountDownTimer
    private val _time = MutableLiveData<Long>()
    val time: LiveData<Long> get() = _time

    val timeString = Transformations.map(time,{ currentTime ->
        DateUtils.formatElapsedTime(currentTime)
    })


    //The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String> get() = _word

    //The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _radioOne = MutableLiveData<String>()
    val radioOne: LiveData<String> get() = _radioOne

    private val _radioTwo = MutableLiveData<String>()
    val radioTwo: LiveData<String> get() = _radioTwo

    private val _radioThree = MutableLiveData<String>()
    val radioThree: LiveData<String> get() = _radioThree

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean> get() = _eventGameFinished

    private val _synonymAnswered = MutableLiveData<Boolean>()
    val synonymAnswered: LiveData<Boolean> get() = _synonymAnswered

    val gameDataAvailable = Transformations.map(word){
        null != it
    }

    val progressBarVisible = Transformations.map(word){
        null == it
    }

    //List of words
    private lateinit var wordList: MutableList<Word>

    private lateinit var correctAnswers: List<String>

    //List of possible answer values
    private lateinit var possibleAnswerList: MutableList<String>

    private var selectedAnswer: Int = 0
    //radio answer list
    //The current word
    private var radioButtonTexts = null
    // val radioButtonTexts: LiveData<List<String>> get() = _radioButtonTexts

    /*** For Coroutine ****/

    //Create a new job
    private var gameViewModelJob = Job()

    //create the scope of the coroutine
    private val uiscope = CoroutineScope(Dispatchers.Main + gameViewModelJob)

    val repository: WordsRepository = WordsRepository(db,application)

    init {
        Log.i("GameViewModel", "GameViewModel created")

        //shuffle the wordlist at the start
        resetWordList()

        _score.value = 0
        _word.value = null
        _eventGameFinished.value = false
        _synonymAnswered.value = false
        _time.value = DONE

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                //_currentTime.value = (millisUntilFinished / ONE_SECOND)
                _time.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _time.value = DONE
                _eventGameFinished.value = true
            }
        }
    }

    /*******     Coroutine Functions   ****************/

    fun resetWordList(){
        uiscope.launch {
            wordList = repository.refreshWordList()
            nextWord()
            timer.start()
        }
    }


    /* Moves to the next word on the list */
    private fun nextWord(){
        //select and remove a word from the list
        if(!wordList.isEmpty()) {

            _synonymAnswered.value = true
            Log.i("GAME_VM", "WordList Empty: " + wordList.isEmpty().toString())
            Log.i("GAME_VM", "Size: " + wordList.size.toString())
            val last = wordList.size - 1
            Log.i("GAME_VM", "Position: " + last.toString())

            val currentWord = wordList.removeAt(last)
            _word.value = currentWord.word
            Log.i("GAME_VM", "Current Word: " + currentWord.word)

            correctAnswers = currentWord.synonyms ?: emptyList()

            uiscope.launch {
                possibleAnswerList = repository.getRadioValues()
                possibleAnswerList.add(correctAnswers.get(0).toString())
                possibleAnswerList.shuffle()

                val size = possibleAnswerList.size
                _radioOne.value = possibleAnswerList.get(size - 1)
                _radioTwo.value = possibleAnswerList.get(size - 2)
                _radioThree.value = possibleAnswerList.get(size - 3)

                _word.value = currentWord.word

                _synonymAnswered.value = false
            }

        }else{
            _eventGameFinished.value = true
        }
    }

    /* Methods for button presses */
    fun onSkip(){

        //if(_score.getValue()!!.compareTo(0) >= 0) {
        if(_score.getValue()!!.compareTo(0) > 0) {
            _score.value = (_score.value)?.minus(1)
        }
        nextWord()
    }

    fun onCorrect(){

        if(evaluateAnswer())
            _score.value = (_score.value)?.plus(1)
        else  if(_score.getValue()!!.compareTo(0) > 0) {
                _score.value = (_score.value)?.minus(1)
        }

        setSelectedAnswer(0)
        nextWord()
    }

    fun onGameFinishedComplete(){
        _eventGameFinished.value = false
    }


    fun evaluateAnswer(): Boolean{
        var result: Boolean  = false
        var answer: String? = null

        when(selectedAnswer){
            1 -> answer = _radioOne.value.toString()
            2 -> answer = _radioTwo.value.toString()
            3 -> answer = _radioThree.value.toString()
        }
        for(e in correctAnswers){
                if (answer === e)
                    result = true
        }
        return result

    }
    override fun onCleared() {
        super.onCleared()

        Log.i("GameViewModel", "GameViewModel destroyed!")
        gameViewModelJob.cancel()
    }

    fun setSelectedAnswer(ans: Int){
        selectedAnswer = ans
    }

    fun checkAnswers(ans: Int){
        setSelectedAnswer(ans)
        onCorrect()
    }

}