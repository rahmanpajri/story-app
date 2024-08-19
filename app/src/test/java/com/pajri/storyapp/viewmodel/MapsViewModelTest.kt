package com.pajri.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.pajri.storyapp.DataDummy
import com.pajri.storyapp.MainDispatcherRule
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.model.LoginSession
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
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainDispatcherRule()

    @Mock
    private lateinit var mapsViewModel: MapsViewModel


    @Test
    fun `when Get Map List Success without Error`() = runTest {
        val loginSessionMapsTest: LoginSession = LoginSession(
            "user-SCXuMdF7ifeJEsLM",
            "cobaaja",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVNDWHVNZEY3aWZlSkVzTE0iLCJpYXQiOjE2NTA5MDQ2MzZ9.eO43m9ZdWw6Wt9u0IXuo_qJaAP96Dp8zDiAhJKXaNJY"
        )
        val dummyMapsListStory = DataDummy.generateDummyMapsList()
        val mapsListStory : LiveData<List<ListStory>> = dummyMapsListStory

        Mockito.`when`(mapsViewModel.getMapsList(loginSessionMapsTest)).thenReturn(mapsListStory)
        val actualMapsListStory = mapsViewModel.getMapsList(loginSessionMapsTest)

        Mockito.verify(mapsViewModel).getMapsList(loginSessionMapsTest)
        Assert.assertEquals(dummyMapsListStory, actualMapsListStory)

    }
}