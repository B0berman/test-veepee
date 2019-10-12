package com.vp.detail.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class MovieDetail(@SerializedName("Title") var title: String = "",
                       @SerializedName("Year") var year: String = "",
                       @SerializedName("Runtime") var runtime: String = "",
                       @SerializedName("Director") var director: String = "",
                       @SerializedName("Plot") var plot: String = "",
                       @SerializedName("Poster") var poster: String = "") : RealmModel

