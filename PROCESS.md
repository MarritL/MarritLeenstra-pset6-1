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
     


