package com.vp.list.viewmodel

import com.google.gson.GsonBuilder
import com.vp.list.BuildConfig
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate


class SearchServiceMock internal constructor(val delegate: BehaviorDelegate<SearchService>): SearchService {
    val data = "{\n" +
            "  \"Search\": [\n" +
            "    {\n" +
            "      \"Title\": \"The Interview\",\n" +
            "      \"Year\": \"2014\",\n" +
            "      \"imdbID\": \"tt2788710\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTQzMTcwMzgyMV5BMl5BanBnXkFtZTgwMzAyMzQ2MzE@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Interview with the Vampire: The Vampire Chronicles\",\n" +
            "      \"Year\": \"1994\",\n" +
            "      \"imdbID\": \"tt0110148\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYThmYjJhMGItNjlmOC00ZDRiLWEzNjUtZjU4MjA3MzY0MzFmXkEyXkFqcGdeQXVyNTI4MjkwNjA@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Interview\",\n" +
            "      \"Year\": \"2007\",\n" +
            "      \"imdbID\": \"tt0480269\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTIwNTA3MTgwNF5BMl5BanBnXkFtZTcwODYxNDk0MQ@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Interview with a Hitman\",\n" +
            "      \"Year\": \"2012\",\n" +
            "      \"imdbID\": \"tt2061712\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTk4NzY0MzU2MF5BMl5BanBnXkFtZTcwOTM5MDY5OA@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"The Interview\",\n" +
            "      \"Year\": \"1998\",\n" +
            "      \"imdbID\": \"tt0120714\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYWNkOTc5MjgtMzM4Zi00MjVjLWFjNWItNmRiMjIzZjgzYTM2XkEyXkFqcGdeQXVyMTMxMTY0OTQ@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"An Interview with God\",\n" +
            "      \"Year\": \"2018\",\n" +
            "      \"imdbID\": \"tt5779372\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTk0ODQ3MzM5Nl5BMl5BanBnXkFtZTgwMzkyOTY4NTM@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Interview with the Assassin\",\n" +
            "      \"Year\": \"2002\",\n" +
            "      \"imdbID\": \"tt0308411\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjAwNDE3MjYxOF5BMl5BanBnXkFtZTYwNTE0MDg5._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Steve Jobs: The Lost Interview\",\n" +
            "      \"Year\": \"2012\",\n" +
            "      \"imdbID\": \"tt2104994\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNjk1NDE5MDExMV5BMl5BanBnXkFtZTcwNDYyMzYwNw@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Interview\",\n" +
            "      \"Year\": \"2003\",\n" +
            "      \"imdbID\": \"tt0360674\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTI1NjI5NTkyMl5BMl5BanBnXkFtZTcwNTIwNDMzMQ@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"The Michael Jackson Interview: The Footage You Were Never Meant to See\",\n" +
            "      \"Year\": \"2003\",\n" +
            "      \"imdbID\": \"tt0361907\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNGUwOTk5NDAtOTA4Yy00NzM0LThiYTktMTJkM2MyNmU0ODBjXkEyXkFqcGdeQXVyMTAwMjY1Mw@@._V1_SX300.jpg\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"totalResults\": \"1096\",\n" +
            "  \"Response\": \"True\"\n" +
            "}";
    override fun search(query: String, page: Int): Call<SearchResponse> {
        val builder = GsonBuilder()
        builder.setPrettyPrinting()
        val gson = builder.create()
        val response: SearchResponse = gson.fromJson(data, SearchResponse::class.java)
        return delegate.returningResponse(response).search(query, page);
    }
}