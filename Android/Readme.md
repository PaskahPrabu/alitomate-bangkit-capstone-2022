<h1 align="center">
  <img align="center" src="/misc/images/icon.png"  width="270"></img>
<br>
Alitomate
</h1>

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

## Mockup
<p align="center">
  <img src="/misc/images/usermockup.png" width="600">
</p>
<p align="center">
  <img src="/misc/images/adminmockup.png" width="600">
</p>

## Requirement
* Android Studio (We are using Android Studio Chipmunk)
* Android Device or Android Emulator with minimum Lollipop Version
* Emulator / External Device
* USB Cable (to Connect Android Device to your Computer)

## Installation Through Android Studio
### 1. Clone this Project to your Computer
```bash
git clone https://github.com/PaskahPrabu/alitomate-bangkit-capstone-2022.git
```

### 2. Open the Project in your Android Studio
Open Android Studio and select open an existing project.

### 3. Run Project in Android Studio
Wait for Gradle Build to Finish and finally press the `Run > Run ‘app’`. Now the app has been installed in your phone / emulator. Make sure that you have configured your android device or emulator 

## Installation Through Apk
- [Admin Version](https://drive.google.com/file/d/15wc3a_kOF-4PFnlQkdpkPAzfFocFOX4K/view?usp=sharing)
- [Api Version](https://drive.google.com/file/d/17UNNj2UdYA6GER3bLB47VegwYh809wWQ/view?usp=sharing)
- [Test Version](https://drive.google.com/file/d/1pIPKkbbKj5EHuqiykbCtT8K17nOf7UkP/view?usp=sharing)
- [User Version](https://drive.google.com/file/d/1LF2OvW46NCDiTYnEOB8Es3FYF5Svr0Pk/view?usp=sharing)

