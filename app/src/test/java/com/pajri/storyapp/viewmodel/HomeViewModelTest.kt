package com.pajri.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.pajri.storyapp.DataDummy.generateDummyStoryResponse
import com.pajri.storyapp.MainDispatcherRule
import com.pajri.storyapp.adapter.StoryAdapter
import com.pajri.storyapp.api.ApiService
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.data.StoryRepository
import com.pajri.storyapp.getOrAwaitValue
import com.pajri.storyapp.model.LoginSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest(){
    private val loginTest:LoginSession = LoginSession(
        "akun-coba",
        "cobacoba",
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVNDWHVNZEY3aWZlSkVzTE0iLCJpYXQiOjE2NTA5MDQ2MzZ9.eO43m9ZdWw6Wt9u0IXuo_qJaAP96Dp8zDiAhJKXaNJY"
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp(){
        homeViewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `when Success Get Story With Not Null and Return Data`() = runTest {
        val dataDummy = generateDummyStoryResponse()
        val dataPaging: PagingData<ListStory> = StoryPagingSource.snapshot(dataDummy)

        val expectedStory = MutableLiveData<PagingData<ListStory>>()
        expectedStory.value = dataPaging
        `when`(storyRepository.getStory(loginTest)).thenReturn(expectedStory)

        val actualStory: PagingData<ListStory> = homeViewModel.getStories(loginTest).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
        Assert.assertEquals(dataDummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val dataPaging: PagingData<ListStory> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStory>>()
        expectedStory.value = dataPaging
        `when`(storyRepository.getStory(loginTest)).thenReturn(expectedStory)

        val actualStory: PagingData<ListStory> = homeViewModel.getStories(loginTest).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStory>>>() {
    companion object {
        fun snapshot(items: List<ListStory>): PagingData<ListStory> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStory>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}