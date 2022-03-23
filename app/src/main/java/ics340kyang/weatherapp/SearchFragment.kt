package ics340kyang.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ics340kyang.weatherapp.databinding.FragmentSearchBinding
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        binding.toolbar.title = "Search"

        viewModel.enableButton.observe(this) { enable ->
            binding.searchButton.isEnabled = enable
        }
        viewModel.showErrorDialog.observe(this) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.searchInputTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val newZip = p0?.toString()
                newZip?.let { viewModel.updateZipCode(it) }
            }
        })

        binding.searchButton.setOnClickListener() {
            val zipInput = binding.searchInputTextBox.text.toString()
            viewModel.updateZipCode(zipInput)
            viewModel.submitZipButton()
            val currentConditions = viewModel.currentConditionCall.value;
            val action = currentConditions?.let { currentConditions ->
                SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                    currentConditions, zipInput
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }
}