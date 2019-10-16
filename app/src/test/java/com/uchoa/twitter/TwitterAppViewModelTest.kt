package com.uchoa.twitter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uchoa.twitter.api.TwitterService
import com.uchoa.twitter.enums.RequestStatus
import com.uchoa.twitter.model.response.AccessTokenResponse
import com.uchoa.twitter.model.response.TweetResponse
import com.uchoa.twitter.model.response.UserResponse
import com.uchoa.twitter.ui.TwitterAppViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class TwitterAppViewModelTest {

    @Mock
    private lateinit var service: TwitterService

    private lateinit var viewModel: TwitterAppViewModel
    private val testScheduler = TestScheduler()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /** The test rule for making the Rxjava synchronously in unit tests */
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        Mockito.`when`(
            service.getAccessToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        ).thenReturn(getAccessTokenResponseMock())

        Mockito.`when`(
            service.getUser(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        ).thenReturn(getUserResponseMock())

        Mockito.`when`(
            service.getTweets(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyString()
            )
        ).thenReturn(getTweetsResponseMock())

        viewModel = TwitterAppViewModel(service, "")
    }

    @Test
    fun `Test get data from api and the mapper between response data and view data`() {
        viewModel.getDataFromUser()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        assert(viewModel.statusData.value == RequestStatus.IDLE)
        assert(viewModel.userData.value?.id == "id_1")
        assert(viewModel.userData.value?.name == "Test 1")
        assert(viewModel.userData.value?.imageProfileUrl == "profile")
        assert(viewModel.userData.value?.imageBannerUrl == "banner")
        assert(viewModel.userData.value?.tweetList?.size == 10)
    }

    private fun getAccessTokenResponseMock(): Observable<AccessTokenResponse> {
        val userResponse = AccessTokenResponse("token_type", "access_token")
        return Observable.just(userResponse)
    }

    private fun getUserResponseMock(): Observable<UserResponse> {
        val userResponse = UserResponse("id_1", "Test 1", "", "", "", 1, 1, "", "banner", "profile")
        return Observable.just(userResponse)
    }

    private fun getTweetsResponseMock(): Observable<MutableList<TweetResponse>> {
        val tweetList = mutableListOf<TweetResponse>()
        for (i in 0..9) {
            tweetList.add(TweetResponse("id_$i", "Test_$i", "Date_$i"))
        }
        return Observable.just(tweetList)
    }
}