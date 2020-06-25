package com.example.gossip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gossip.service.Model
import kotlinx.android.synthetic.main.news_item.view.*


class NewsListAdapter(val context: Context): RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {
    var listOfArticles: List<Model.Article> = listOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titleTV = view.newsTitle
        val descriptionTV = view.newsDescription
    }

    fun setData(newNews: List<Model.Article>){
        listOfArticles = newNews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listOfArticles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTV.text = listOfArticles.get(position).title
        holder.descriptionTV.text = listOfArticles.get(position).description
    }
}