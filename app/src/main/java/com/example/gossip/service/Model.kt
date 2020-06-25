package com.example.gossip.service

import com.google.gson.annotations.SerializedName


object Model {
    data class Result(@SerializedName("articles") var articles: List<Article>) : IResponse
    data class Article(@SerializedName("title")var title: String,
                        @SerializedName("description")var description: String,
                        @SerializedName("content") var content: String)
    data class ErrorResponse(var errorMessage: String) : IResponse
}