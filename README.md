# Introduction

Hello future colleague!

We are a bunch of developers that like to be surrounded with creative and curious people, who thrive on sharing their experiences and learning continuously.
If you are here, it's because you probably have applied to vente-privée as an Android Developer. If not we encourage you to do so, through our [careers site](https://careers.vente-privee.com/en/home-page-en/), we have some open positions for you.
We know that your time is precious, and we don't want you to waste any of it. 
That's why we provide you with this skeleton project so you can show off your dazzling abilities by completing a few challenges listed below.
If you have any suggestions or questions related to this repository, you can contact us or open an issue. We will be happy to answer you.

Good luck!

# About the project
The project uses [OMDb API](http://www.omdbapi.com/) to display a list of movie posters and some details about the selected movie like the runtime, director or the release year.

![Screenshot 1](images/screenshot_1.png)
![Screenshot 2](images/screenshot_2.png)

It contains three gradle modules called `list`, `detail` and `favorites`. `List` module is written in Java and `detail` module is written in Kotlin. 
We left `favorites` module empty to let you make choice of language.

![Diagram](images/diagram.png)

### Technologies
* Architecture - MVVM with [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* Programming languages - Java 8, Kotlin 1.2.60
* Dependency injection - [Dagger 2](https://github.com/google/dagger)
* Images loading - [Glide](https://github.com/bumptech/glide)
* HTTP client - [Retrofit](https://square.github.io/retrofit/)

# Prerequisites
1. [Duplicate this repository](https://help.github.com/articles/duplicating-a-repository/) - please do not fork it ⚠️
2. Generate an API Key. You can do it from [here](http://www.omdbapi.com/apikey.aspx)

# Challenges
1. The Wrong State - we took care of fetching the data remotely from the api. So the app is supposed to show a list of posters, but instead it still displays the progress bar. Can you find where the problem is and fix it? And to make sure we won't make the same mistake twice write a simple unit test.
2. The Lost Event - when the user clicks on an item from the movies list, the app is supposed to display some information about the selected movie (this feature is located in the `detail` module). Currently the app doesn't respond to clicks, can you please fix it?
3. The Lost State - the app comes with a search bar to help users find their favorite movies. Unfortunately, there is a bug. When we rotate the screen, the app clears the text we just typed. Can you provide a solution to prevent this state loss from happening on rotation.
4. Some refreshments - we made sure that this app handles networking errors. But we didn't implement any mechanism to reload the data, without quitting the app. Can you provide a way of refreshing the list of movies?
5. The chosen ones - the favorites screen should show a list of the user's favorite movies. Try to implement this feature. Remember that the list of favorite movies should be available even after killing the app.
6. The Shrink - first start by obfuscating the application using Proguard. Now you should have an empty details view in the app, your mission is to fix these issues. Now the apk is smaller, but we know it can be even smaller, use the apk analyzer to find out how to do so.

### Bonus:
You cracked all of those challenges, and you still can't get enough. We've got you covered, we have some bonus challenges, if you want some extra points, or you can just ignore them and go grab a coffee.

1. Memory leaks - There is a memory leak. Try to find it and fix it.
2. Java to Kotlin conversion - convert `list` module from Java to Kotlin.
3. List loading indicator - The app loads gradually the list of movies. Add a progress bar to indicate that the next page is loading.

# Responses

First of all, thanks for this exercise. It is kind of complete and really educative.

### Challenges responses
1. The list was not updated with the service response. You add to call `movies.value = SearchResult.success(aggregatedItems, it.totalResults)` in `ListViewModel` 
2. To handle the poster click, I used the same kind of implemention than it was done for favorites button click in `ListFragment.initBottomNavigation` : 
```kotlin
    override fun onItemClick(imdbID: String) {
        val uri = Uri.parse("app://movies/detail?imdbID=$imdbID")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }
```
3. To resolve the lost state, I have used `onSavedInstanceState` and `onCreate` methods from `MovieListActivity` using the bundle.
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        searchViewQuery = savedInstanceState.getString(SEARCH_VIEW_QUERY)
    }
```
```kotlin
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_VIEW_QUERY, searchView?.query?.toString())
    }
```
4. To handle errors, I changed the error TextView that was included in `ListFragment` and `DetailActivity` with a refresh layout :
```xml
    <LinearLayout
        android:id="@+id/errorContainer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:orientation="vertical">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/data_loading_error"
            android:padding="4dp"
            android:textColor="#333" />

        <androidx.appcompat.widget.AppCompatTextView
            android:id="@+id/errorReload"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:drawableEnd="@drawable/ic_reload"
            android:gravity="center_vertical"
            android:drawablePadding="4dp"
            android:padding="4dp"
            android:text="@string/try_to_reload"
            android:textColor="#333" />
    </LinearLayout>
```
Then I bound this layout with `view.findViewById<View>(R.id.errorReload).setOnClickListener { listViewModel.searchMoviesByTitle(currentQuery, 1) }`

5. I used a SQLDataBase to store the favorite movies : `FavoritesSQLDataBaseHelper`. It will be provided by the module `com.vp.movies.di.FavoritesStorageModule` in app gradle module.
`providesFavoritesStorageAccessor` : provides the access to save or remove favorites from storage and know if the movie is already in the favorite database to details gradle module.
`providesFavoritesStorageGetter` : provides all the favorites movies to feature gradle module.
I proposed a layout for favorites movies that is voluntary different than in the list module to show you my abilities to build a layout.
Finally a click on a favorite movie will lead to `DetailActivity`

6. To fix the proguard I simply added the `@SerializedName("imdbID")` in ListItem.
Without this annotation, le `imdbID` was not parsed with `Gson` since `Gson` try to parse it with the name of the field which was proguarded.

### Bonus responses
1. The companion object (which is a singleton) `DetailActivity` kept the instance of `QueryProvider`. This `queryProvider` was actually `DetailActivity` and so the Activity was leaked. 
I changed the `queryProvider` to be a field of `DetailActivity`. 
This leak was easily found by using LeakCanary.

2. Nothing to tell excepting : it's done

3. I added an progress bar item to the list while loaded with a span size equals to the layout manager span count. (see `ListFragment` and `ListAdapter`)
