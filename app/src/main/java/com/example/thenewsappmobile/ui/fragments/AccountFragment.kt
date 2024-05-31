package com.example.thenewsappmobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.example.thenewsappmobile.R
import com.example.thenewsappmobile.ui.SignInActivity
import com.example.thenewsappmobile.databinding.FragmentAccountBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountBinding.bind(view)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            Glide.with(this).load(user.photoUrl).into(binding.profileImage)
            binding.bio.setText("Hi we are Hoang Anh - Hieu Anh - Quang Anh from HCMUTE, this is our Mobile Programming course final project.")
            binding.userid.text = user.uid
            binding.username.text = user.displayName
            binding.email.text = user.email
            binding.phoneNumber.text = user.phoneNumber

        } else {
            // Handle the case where the user is not signed in
        }

        binding.logoutButton.setOnClickListener {
            signOutAndStartSignInActivity()
        }
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}