package com.example.gossip

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gossip.databinding.ActivityMainBinding
import com.example.gossip.service.Model
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Restart/show progress dialog if it was showing before config change
        if(savedInstanceState?.getBoolean(Constants.DIALOG_KEY) == true){
            showProgressDialog()
        }
        //Initialize databinding and viewmodel
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //Initialize Adapter and assign layout manager to recycler view for list of news
        val adapter = NewsListAdapter(this)
        adapter.setData(viewModel.caseListOfNews)
        recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.responseMutableLiveData.observe(this, Observer {
            if(it!=null){ //Work around to stop livedata posting value on config change
                stopProgresssIndicators()
                if(it is Model.Result){
                    adapter.setData(it.articles)   //Assign new list to adapter so it can be shown in the recyclerview
                }else if(it is Model.ErrorResponse){
                    Toast.makeText(applicationContext,it.errorMessage,Toast.LENGTH_SHORT).show() //Show toast with error message
                }
                viewModel.responseMutableLiveData.postValue(null) //Work around to stop livedata posting value on config change
            }

        })

        binding.searchBtn.setOnClickListener { showProgressDialog()
            fetchLatestNews() }


        binding.swipeRefresh.setOnRefreshListener { fetchLatestNews() }
    }

    //Save instance state of progress dialog showing so we can recreate it if config change occurs while dialog showing
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.run {
            putBoolean(Constants.DIALOG_KEY,progressDialog!!.isShowing)
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    //Call to view model to gather news if search input is not empty
    private fun fetchLatestNews(){
        val input = binding.searchET.text.toString()
        if(input.isNotEmpty()){
            viewModel.getHeadlines(input)
        }else{
            Toast.makeText(applicationContext,getString(R.string.invalid_input_msg),Toast.LENGTH_SHORT).show()
        }
    }

    //Shows progress dialog to user as we gather news
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(getString(R.string.progress_dialog_msg))
        progressDialog!!.setTitle(R.string.progress_dialog_title)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    //Stops any progress indicators once news is fetched
    private fun stopProgresssIndicators(){
        if(binding.swipeRefresh.isRefreshing){
            binding.swipeRefresh.isRefreshing = false
        }
        if(progressDialog!=null && progressDialog!!.isShowing){
            progressDialog!!.cancel()
        }
    }

}
