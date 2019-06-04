package com.example.wordmaster


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordmaster.database.WordMasterDatabase
import com.example.wordmaster.databinding.FragmentGameBinding
import com.example.wordmaster.screens.game.GameViewModel
import com.example.wordmaster.screens.game.GameViewModelFactory

/*
*  A fragment which shows the words and prompts the user to enter the correct sysnonym,
*  It keeps track of the correct answers and displays the score and the timer.
*
* */

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: FragmentGameBinding

    /*
    * Called when the fragment is ready to display content on the screen
    * This function uses DatabindingUtil to inflate R.layout.fragment_game
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.setTitle(getString(R.string.this_app_name))
        //Get a reference to the binding object and inflate the fragment views
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game ,container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = WordMasterDatabase.getInstance(application).wordDatabaseDao

        val viewModelFactory = GameViewModelFactory(dataSource,application)

        //get a reference to the game view model from the provider. lifecycle library creates the view model
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel

        //the binding can now observer LiveData updates
        binding.setLifecycleOwner(this)

        binding.radioAnswer.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioVal1 -> viewModel.setSelectedAnswer(1)
                R.id.radioVal2 -> viewModel.setSelectedAnswer(2)
                R.id.radioVal3 -> viewModel.setSelectedAnswer(3)
            }

        }


        viewModel.synonymAnswered.observe(this, Observer { hasAnswered->
            if(hasAnswered) {
                binding.radioAnswer.clearCheck()
            }
        })

        viewModel.eventGameFinished.observe(this, Observer { hasFinihsed->
            if(hasFinihsed) {
                gameFinished()
                viewModel.onGameFinishedComplete()
            }
        })

         return binding.root
    }


    /* Method called when game is finished */
    private fun gameFinished(){

        val arg = viewModel.score.value ?: 0

        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(arg)

        Navigation.findNavController(binding.root).navigate(action)
    }


}
