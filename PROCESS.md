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
       
     


