package com.pajri.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.pajri.storyapp.DataDummy
import com.pajri.storyapp.MainDispatcherRule
import com.pajri.storyapp.api.LoginResponse
import com.pajri.storyapp.api.RegisterResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainDispatcherRule()

    @Mock
    private lateinit var authViewModel: AuthViewModel

    @Test
    fun `when Register Success without Error`() = runTest {
        val registerName = "name"
        val registerEmail = "name@mail.com"
        val registerPassword = "12345678"

        val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
        val registerResponse : LiveData<RegisterResponse?> = dummyRegisterResponse

        Mockito.`when`(authViewModel.registerLauncher(registerName, registerEmail, registerPassword)).thenReturn(registerResponse)
        val actualRegisterResponse = authViewModel.registerLauncher(registerName, registerEmail, registerPassword)

        Mockito.verify(authViewModel).registerLauncher(registerName, registerEmail, registerPassword)
        Assert.assertEquals(dummyRegisterResponse, actualRegisterResponse)

    }

    @Test
    fun `when Login Success without Error`() = runTest {
        val emailExist = "test@mail.com"
        val password = "12345678"
        val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
        val loginResponse : LiveData<LoginResponse?> = dummyLoginResponse

        Mockito.`when`(authViewModel.loginLauncher(emailExist, password)).thenReturn(loginResponse)
        val actualLoginResponse = authViewModel.loginLauncher(emailExist, password)

        Mockito.verify(authViewModel).loginLauncher(emailExist,password)
        Assert.assertEquals(dummyLoginResponse, actualLoginResponse)
    }

}