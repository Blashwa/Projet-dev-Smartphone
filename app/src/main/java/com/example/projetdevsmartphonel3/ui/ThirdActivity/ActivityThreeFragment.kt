package com.example.projetdevsmartphonel3.ui.ThirdActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetdevsmartphonel3.R

class ActivityThreeFragment : Fragment() {

    private lateinit var notificationsViewModel: ThirdViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(ThirdViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_third, container, false)
        return root
    }
}