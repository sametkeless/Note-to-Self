package com.tolerans.notetoself.ui.register

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
import com.tolerans.notetoself.databinding.FragmentRegisterBinding
import com.tolerans.notetoself.db.entities.User
import com.tolerans.notetoself.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

private var _binding : FragmentRegisterBinding?=null
private val binding:FragmentRegisterBinding get() = _binding!!


@InternalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
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
        btnSignUp.setOnClickListener {
           lifecycleScope.launchWhenCreated {
                   val user = User(username = txtInputEdittextRegisterUsername.text.toString(), password = txtInputEdittextRegisterPassword.text.toString())
                   viewModel.saveUser(user)
                   viewModel.status.observe(viewLifecycleOwner, Observer {resource->
                       when(resource){
                           is Resource.Loading ->{
                               progressbarRegister.visibility = View.VISIBLE
                           }
                           is Resource.Failed ->{
                               progressbarRegister.visibility = View.GONE
                               Toast.makeText(requireActivity(),resource.message,Toast.LENGTH_SHORT).show()
                           }
                           is Resource.Success ->{
                               progressbarRegister.visibility = View.GONE
                               Toast.makeText(requireActivity(),"Başarılı",Toast.LENGTH_SHORT).show()
                               findNavController().navigate(
                                       RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                           }
                           
                       }

                   })

           }
        }
        btnRegistertoLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

}