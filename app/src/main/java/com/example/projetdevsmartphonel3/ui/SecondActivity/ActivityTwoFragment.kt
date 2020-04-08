package com.example.projetdevsmartphonel3.ui.SecondActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.MainActivity
import com.example.projetdevsmartphonel3.R

class ActivityTwoFragment : Fragment() {

    private lateinit var dashboardViewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(SecondViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_second, container, false)


        val buttonClear : Button = root.findViewById(R.id.clearButton)

        val activity : MainActivity = getActivity() as MainActivity

        buttonClear.setOnClickListener{ activity.clearMap()}
        dashboardViewModel.text.observe(this, Observer {

        })
        return root
    }
}