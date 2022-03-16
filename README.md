# media_handler

There are "media" (id, type, uri) and "media_video" (id, duration) t*ables in the database (you may add tables/columns).

There is a singleton service* VideoDurationCalculationService with 1 method "calc" in the spring boot app. 

In the beginning, It spends a random amount of seconds, between 1 and 10. Then it returns a random integer value between 
O and 60. "calc" method should not execute more than 1 calculation at the same time.

The app has the 1st endpoint that receives id, type, and url:
- Id is not null and 36 characters long string.
- Type is an enum of possible values IMAGE and VIDEO.
- Url is nullable and less than 255 characters.

The endpoint saves all valid information into table "media".

If the type is VIDEO and URL is not nullable and "media_video" doesn't contain information about this media id, then
- endpoint returns immediately
- duration should be calculated in the background and saved into the "media_video" table.
- 
Assume that repeated duplicate queries (all 3 fields are the same) are possible.

Assume that the same id can contain only the same type and uri
(if id=1,type=IMAGE,url= http://asdf.com exists in the system then
id=1,type=IMAGE,url= http://example.com is unacceptable and endpoint should return with an error)

The app has a 2nd endpoint that returns media information (JSON) by id. Handle duration information somehow to separate:
- Video media without URL and hence without duration
- Video media with URL but duration is not calculated yet
- Video media with URL and with duration