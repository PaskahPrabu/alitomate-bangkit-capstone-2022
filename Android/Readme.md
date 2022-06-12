## About Project
Alitomate is a fire identification and reporting app that takes advantage of machine learning and GPS to report a fire accident with the hope of the user can report a fire accident easier. Machine learning implemented on the app is used for identifying fire in an image and filtering it accordingly to prevent false reports. While GPS is used for the responder to find the report location easier and get to the location faster. We also provide an emergency call button in case there is an obstruction resulting in the user cannot submit a report.

## About Version
- **AdminVersion** is an app that is suited for responder/admin. It provides a list of reports that when selected will open a Google Maps so that they can make a route to the destination of a report. Another feature is it can also display a map so that they can see where are the location that vulnerable to fire accidents.
- **ApiVersion** is an app for user. The difference is in this version uses an API to submit a report without direct connection to Firebase, and it also contains gallery button feature to retrieve an image.
- **TestVersion** is an app for testing purposes. The only difference with UserVersion is that in TestVersion it contains a gallery button for easier TFlite testing. It also uses a direct connection to Firebase Storage and Database.
- **UserVersion** is an app for the user that has been optimized in terms of database integration. It uses Firestore as a database to store a report and Firebase Storage to store an image and then passes the link to Firestore together with the report.

## Main Feature
- **User App**
  - [x] SplashScreen
  - [x] TFLite Machine Learning
  - [x] CameraX
  - [x] Firebase Storage
  - [x] Firebase Firestore
- **Admin App** 
  - [x] SplashScreen
  - [x] Maps
  - [x] Firebase Firestore
