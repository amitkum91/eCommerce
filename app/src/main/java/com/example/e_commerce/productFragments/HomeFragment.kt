package com.example.e_commerce.productFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.activity.AllOrderActivity
import com.example.e_commerce.databinding.FragmentHome2Binding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHome2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(layoutInflater)

        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_categoryFragment)
        }

        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_productFragment)
        }

        binding.button3.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_sliderFragment)
        }

        binding.button4.setOnClickListener{
            startActivity(Intent(requireContext(), AllOrderActivity::class.java))
        }

        return binding.root
    }
}