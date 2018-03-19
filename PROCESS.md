# PROCESSBOOK 
Final Project Minor Programmeren
Marrit Leenstra

# 19-3-2018
* Worked on unsubscribe
  * Problem: app crashes when trying to delete from database
    * Reason: data is still used in MainActivity after deleting from database
      * Solution: Check if mUser is not null in MainActivity
  * Problme: PERMISSION DENIED error when trying to delete data from database
    * Reason: User was already deleted from authentication before deleting from database 
      * Solution: First delete data from database and then the user from authentication
