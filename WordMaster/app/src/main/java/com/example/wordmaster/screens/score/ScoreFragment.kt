package com.example.wordmaster


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordmaster.databinding.FragmentScoreBinding
import com.example.wordmaster.screens.score.ScoreViewModel
import com.example.wordmaster.screens.score.ScoreViewModelFactory


class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.this_app_name)

        // Inflate the layout for this fragment
        val binding: FragmentScoreBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_score ,container, false)

       //   val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

        viewModelFactory = ScoreViewModelFactory(ScoreFragmentArgs.fromBundle(arguments!!).score)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoreViewModel::class.java)

        binding.scoreViewModel = viewModel

        //the binding can now observer LiveData updates
        binding.setLifecycleOwner(this)


        viewModel.eventTryAgain.observe(this, Observer { hasChanged ->

            if(hasChanged) {
                Navigation.findNavController(binding.root)
                    .navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
               // Navigation.findNavController(binding.root).navigate(R.id.action_scoreFragment_to_gameFragment)
                viewModel.onTryAgainComplete()
            }

            //Navigation.findNavController(it).navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        })

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.winner_menu, menu)

        //check to see if the intent resolves to an activity
        if(null == getShareIntent().resolveActivity(activity!!.packageManager)){
            //hide the menu item if it doesn't resolve
            menu?.findItem(R.id.share).setVisible(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.share -> shareSuccess()
        }

        return super.onOptionsItemSelected(item)

    }

      private fun getShareIntent(): Intent {

          val shareIntent = ShareCompat.IntentBuilder.from(activity)
                                .setType("text/plain")
                                .intent

          return shareIntent
      }

    private fun shareSuccess(){
        startActivity(getShareIntent())
    }
}
