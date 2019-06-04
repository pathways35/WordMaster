package com.example.wordmaster


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.wordmaster.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.setTitle(getString(R.string.this_app_name))

        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_title ,container, false)

        binding.startButton.setOnClickListener {
            Navigation.findNavController(it).navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }

        //setup menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        //inflate the menu
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    //Returns true if NavigationUI.onNavDestinationSelected returns true, else returns super.onOptionsItemSelected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, Navigation.findNavController(view!!))
                || super.onOptionsItemSelected(item)
    }
}
