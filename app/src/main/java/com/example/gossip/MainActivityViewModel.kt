package com.example.gossip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gossip.service.IResponse
import com.example.gossip.service.Model
import com.example.gossip.service.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel: ViewModel() {
    // The MutableLiveData that stores the most recent response
    val responseMutableLiveData = MutableLiveData<IResponse>()

    //Cache of list of news to repopulate recyclerview on config change
    var caseListOfNews: List<Model.Article> = listOf()

    //Singleton initialization of NewsService
    val newsService : NewsService by lazy {
        NewsService.create()
    }

    //Call service and handle response and failure
    fun getHeadlines(query: String) {
        newsService.getHeadlines(query).enqueue(
            object : Callback<Model.Result> {
                override fun onFailure(call: Call<Model.Result>, t: Throwable) {
                    responseMutableLiveData.postValue(Model.ErrorResponse(t.localizedMessage?:"Error: Something went wrong!"))
                    caseListOfNews = listOf()
                }
                override fun onResponse(
                    call: Call<Model.Result>,
                    response: Response<Model.Result>
                ) {
                    responseMutableLiveData.postValue(response.body()?:generateGenericErrorResponse())
                    if(response.body()!=null){
                        caseListOfNews = response.body()!!.articles
                    }
                }
            }
        )
    }

    //Create a generic error response
    fun generateGenericErrorResponse() = Model.ErrorResponse("Error: Something went wrong!")

}