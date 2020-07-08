package me.atyrlik.plmtalexandre.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import me.atyrlik.plmtalexandre.databinding.MainFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!! // this is from the Android developer guideline

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.measures.observe(viewLifecycleOwner, Observer {
            Log.d("mooo", it.toString())
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.startRegisteringMeasure()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}