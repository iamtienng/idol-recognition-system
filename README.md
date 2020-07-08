# Idol Recognition System

### Ideal
Have you ever browsed social networking, see a meme that has the face of a celebrity, assuming that person is a very pretty person and you want to find more information about them? You go through a lot of comments to find someone commenting their name but there is no information, and everyone seems already know this person. So, you have no information what that character. What would you do in this situation?
From that idea, I decided to go to the Microsoft API to create a celebrity database, whenever I saw an image on the internet, I could find who they are, their specific information.

### Pre-requisites
* NetBeans 8.2 with JDK 8u251
* MongoDB Localhost Server
* Azure Bing Image Search API Subscription Key
* Azure Face API Subscription Key

### Database Prepares
* Create a database named idolrecognition
* Import 5 collections by 5 json files in MongoDB folder

### Installation
* Open 3 Projects in NetBeans and run
* Locate Java Library in Library folder if needed
* Change your Face API Subscription Key and Bing Image Search API Subscription Key in IdolrecognitionserverApplication.java
* Idolrecognitionserver
* IdolRecognitionClient
* IdolRecognitionAdmin

### Using
* In Admin Application, you can add new Idol and also edit or delete Idol and User.
* In Client Application, you can recognize an Idol by an URL, see what you found, delete history and your profile.

### Credits
- The Idol Recognition System is a full-stack project built with Spring and Swing developed by Tien Nguyen.
- Initial concept and development was done by Tien Nguyen.
