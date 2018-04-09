# PROCESSBOOK 
Final Project Minor Programmeren
Marrit Leenstra

# 19-3-2018
* Worked on unsubscribe
  * Problem: app crashes when trying to delete from database
    * Reason: data is still used in MainActivity after deleting from database
    * Solution: Check if mUser is not null in MainActivity
  * Problem: PERMISSION DENIED error when trying to delete data from database
    * Reason: User was already deleted from authentication before deleting from database 
    * Solution: First delete data from database and then the user from authentication

# 30-3-2018
* Working on API request from Yummly
  * Problem: don't know how to get the result back from my helperclass in fragement 

# 3-4-2018
* Working on API request from Yummly
  * Problem: don't know how to get the result back from my helperclass in fragement 
     * Solution: Not Callback but FragmentCallback.....
* Working on downloading the pictures that get back from the API request
  * Problem: couldn't connect to url via: (InputStream) new URL(url).getContent();
     * Solution: Looked on the internet for a library that downloads the pictures for me.
       Using now the picasso library: http://square.github.io/picasso/ 
       Works perfectly.

# 4-4-2018
* Considering the different task i want my app to perform outside the scope of the lifecycle:
1. Reminder to the user to let the app know if he ate vegetarian (ca. 19.00 O'clock)
2. Count as a NO if the user didn't click that day (in the night)
3. Make the buttons visible again (were invisible after the user clicked) (in the night)
4. Get a new receipt (or receipts?) from YUMMLY for the new day (in the night)
* So i have 4 task at two different times: one at ca 19.00 and three in the night
  * Problem: which schedulers should i use for these tasks?
     * Solution: I looked up some info about schedulers on internet. (i.e. https://developer.android.com/training/scheduling/alarms.html,
       https://stackoverflow.com/questions/23808434/android-gcm-vs-alarms-for-notifications and
       https://www.bignerdranch.com/blog/choosing-the-right-background-scheduler-in-android/). Basically AlarmManager from Android is 
       recommanded for operations that do not need networking and at inexact time (so number 1 of my tasks and possibly number 2 and 3).
       However networking is better via JobScheduler or GCM Network Manager, because it is less exact and therefor better for the user
       (battery life). JobScheduler needs API level 21 or higher and my app has minimum 18, so i am going to try the GCM Network Manager. 
       Since task 2, 3 and 4 can be performed at the same time, i am going to try to implement them all in the same alarm.
     * Update: when i wanted to start implementing the GCM I read that Firebase Cloud Messaging (FCM) is the new version of GCM and it 
       is strongly recommanded to use the Firebase JobDispatcher instead. So after reading the documentation: 
       https://github.com/firebase/firebase-jobdispatcher-android#user-content-firebase-jobdispatcher- This looks like the best choice.
   * Problem: with the Firebase JobDispatcher you can't set an initial time at all. 
   
# 5-4-2018
* Going on with problem of yesterday
  * Problem: with the Firebase JobDispatcher you can't set an initial time at all. 
     * Solution: back to the AlarmManager. Since i want to download only a small amount of data and only once a day, i think it won't be a too big restraint on the battery. I really want the app to update it's content in the nighttime.
* Once the receipts are downloaded (in the night), they need to be displayed in the hometab.
  * Problem: where are the recipes going to be saved?
     * Solution: so far in the saved preferences, this way they will be persistent and can be updated every night
  * Problem: How are the recipes going to be displayed?
     * I am considering cards or gridlist. Gridlist is a little simpler design with less text, every grid should be comparable. Whereas cards can be expandble and contain more action options and are typically less homogeneous. I want to display a picture of the recipe with the name and rating. However, the use of the yummly api requires to display the source name and a link to the source. Since the recipes are pretty homogeneous i will choose gridlist.
* Images are now loading in the gridview
  * Problem: The pictures of the recipes are very small. I thought to display one recipe along the whole width of a phone in portrait mode, but the pictures are too small. Considering another way of downloading the recipes from yummly (one-per-one, with option of bigger images).
     * Solution: I stick to the gridView, because i think that is the right choice for homegeneous data (according to the design guide). I display now more recipes on the homepage, which might actually be also nicer.
     
# 6-4-2018
* Working on displaying recipes when user logs out and logs in again
  * Problem: When the user logs out, the shared preferences are lost and therefor the recipes are lost
     * Solution: saving the recipes in the firebase database (on the users account).
       I saved the listarray as a string (by using the Gson library), because it works good and easier than parcebles. 
* Working on dispalying recipes also when the user logs in for the first time
  * Problem: alarm to start downloading recipes is set for the night, so when the first launch is before, nothing is downloaded yet.
     * Solution: On first launch also downlaod recipes (at the same place as the alarms are set = MainActivity).
  * Problem: when another user logs in on the same phone the firstlaunch boolean is already true (not associated with account?), so the recipes are again not downloaded.
     * Solution: I moved the first time download to the register page. So when a user registers, the first recipes will be downloaded right away and put under his name in the firebase database.
     
# 8-4-2018
* Working on searching the recipes
  * Problem: recipes are always the same.
     * Solution: Add a list of ingredients to assets folder (or database?) and choose everytime a random ingredient to add to the query.
  * Problem: recipes are too simular 
     * Solution: Make a requet with 2 ingredients (... OR ...) 
     * Update: OR doesn't work. At the moment i don't have a solution. Better 10 recipes with carrot then every time the same recipes.
* Working on runstreak
  * Problem: when a user doens't eat vegetarian he might not bother to click no in the app
     * Solution: Add functionality to check if user clicked or not in one day, if not, consider as a "NO" and set runstreak to 0.
* Now that every account has also the boolean clickedToday, also the community value for number of people eating vegetarian today can be updated.
  
       
     


