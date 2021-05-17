package com.tolerans.notetoself.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tolerans.notetoself.R
import com.tolerans.notetoself.databinding.FragmentLoginBinding
import com.tolerans.notetoself.db.entities.User
import com.tolerans.notetoself.util.AppExtensions.hideKeyboard
import com.tolerans.notetoself.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding?=null
    private val binding: FragmentLoginBinding get() = _binding!!

    val viewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    fun initViews() = with(binding){
        btnLogingoRegisterFragment.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        btnLogin.setOnClickListener {
            requireView().hideKeyboard()

            lifecycleScope.launchWhenCreated {
                val user = User(username = txtInputEdittextLoginUsername.text.toString(),password = txtInputEdittextLoginPassword.text.toString())
                viewModel.login(user)
                viewModel.user.observe(viewLifecycleOwner, Observer { resource->
                    when(resource){
                        is Resource.Loading -> {
                            progressbarLogin.visibility = View.VISIBLE
                        }
                        is Resource.Failed -> {
                            progressbarLogin.visibility = View.GONE
                            Toast.makeText(requireContext(),resource.message,Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Success ->{
                            progressbarLogin.visibility = View.GONE
                            if (findNavController().currentDestination?.id == R.id.loginFragment) {
                                findNavController().navigate(
                                        LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                            }

                        }
                    }
                })
            }
        }
    }

}