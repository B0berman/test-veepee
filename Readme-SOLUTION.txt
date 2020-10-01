Here is a summary of developments.

Challenge 1
	Updated movies liveData when retrofit response is successful and added relative test case for successfull situation.

Challenge 2
	Added logic to the onItemClick in order to start DetailActivity passing movie id as a string parameter.

Challenge 3
	Fix search bar bug that was happening on rotation. Query text is now saved in the Bundle and persist on rotation.

Challenge 4
	Added a SwipeRefreshLayout that is actionable scrolling down. Logic behind it is in the ListFragment where has been added a setupSwipeRefresh method.

Challenge 5
	Created new favorite screen using Kotlin.
	Added functionality in the Detail screen to mark a movie as favorite. It uses SharedPreferences to save movie ids locally in the device.
	Created a RecyclerView for favorite movies. Similar to the one in the list module to be consistent around the app.
	Use RxJava to make multiple Retrofit calls. It's necessary because OMDb doesn't allow to retrieve a list of movies giving a list of ids.
	A SwipeRefreshLayout is available in this screen as well.
	
Challenge 6
	Switched to the debugProguard build variant.
	In my case the empty details bug was not present cause I accidentally fixed it with previous changes.
	I managed to reproduce it switching to a previous branch and it can be fixed adding to the App/prougard-rules.pro this line:
		-keep class com.vp.list.model.ListItem {*;}
	Using the apk analyzer I found out and deleted a big image not used by the app. The apk size drastically decreased after removing it.
	Added shrinkResources = true in the app build.gradle file.

Bonus 1
	Found memory leak using LeakCanary library.
	It was caused by the DetailsViewModel holding an DetailActivity instance.
	Fixed it using dagger in order to provide a string parameter containing the movieId to the ViewModel.

Bonus 2
	Converted list module from Java to Kotlin.
	
Bonus 3
	Added a progress bar that show up when downloading a new page of movies.
	The progress bar is visible right above the BottomNavigationView when dowloading takes more than 0.5 seconds.
	
	
For any additional question please contact me at stefano.serra23@gmail.com