# CodeShare

A website to share code made using Spring Boot. Nearly at the end of development.
Open for contributions.

# Features that I want to implement:
- [X] Swagger for API docs
- [X] Deleting all posts of a user if a user is deleted
- [X] Setting time limit so that code will be deleted if time limit is reached
- [X] Setting view limit so that code will be deleted if view limit is reached
- [X] Hashing of password for security

# How to install:
In terminal bash: 
````
git clone https://github.com/ritish78/CodeShare.git
````

After the clone is sucessfull, then add this project as 'New Project from Existing Source' (depending upon the IDE) and add 'Maven' . Then, in terminal:

````
mvn clean install
````

Then run the 'CodeSharePlatformApplication.java' or in terminal:
````
mvn spring-boot:run
````

Tomcat should start on port 8080.

# API Documentation from Swagger:
After running the spring boot application, go the url:
````
http://localhost:8080/v2/api-docs
````
Or, to get the info in UI from Swagger, go the url:
````
http://localhost:8080/swagger-ui.html
````

# Rest end points available as of today:
1. GET      /code/new
1. POST     /code/new
1. GET      /code/{uuid}
1. GET      /code/latest
1. GET      /code/last/{count}


# Rest end points for api:

For Code:
1. POST     /api/code/new
1. GET      /api/code/{uuid}
1. GET      /api/code/latest
1. GET      /api/code/last/{count}
1. POST     /api/code/addAll
1. DELETE   /api/code/{uuid}

For User:
1. POST     /api/user/new
1. GET      /api/user/{uuid}
1. DELETE   /api/user/{uuid}
1. GET      /api/user/{uuid}/code
1. POST     /api/code/{uuid}/new

You can create a User and create as many posts(Code in this website).
# To create a new User, send a POST request to: localhost:8080/api/user/new with JSON:
````
{
    "username": "test",
    "email": "test@testing.com",
    "password": "myPassword#1"
}
````
If the user is created suceessfully, the server will send a JSON reponse with the UUID of created user.
````
{
    "uuid": "6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d"
}
````

# To delete a User, send a DELETE request to: localhost:8080/api/user/{uuid}
The server will send a JSON file of deleted User's uuid.
FEATURE: If a user has created posts, and if you delete the user then the associated posts of the user will also be deleted.
````
{
    "uuid": "6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d"
}
````

# To create a post (Code) for user, send a POST request to: localhost:8080/api/code/{uuid}/new with JSON:
````
{
    "body": "Creating a post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
    "viewsLeft": 5,
    "timeInSeconds": 120
}
````
If post is saved sucessfully, the server will send a JSON response with the UUID of created post by the user.
````
{
    "uuid": "b2ab3374-b7c0-4818-b6b7-6c6011abb47d"
}
````
# To get all the posts created by the user, send a GET request to: localhost:8080/api/user/{uuid}/code.
```
[
    {
        "body": "Creating a post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
        "dateTime": "2021-01-16 10:41:16",
        "viewsLeft": 5,
        "timeInSeconds": 120
    },
    {
        "body": "Creating another post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
        "dateTime": "2021-01-16 10:41:17",
        "viewsLeft": 5,
        "timeInSeconds": 120
    },
    {
        "body": "Creating third post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
        "dateTime": "2021-01-16 10:41:18",
        "viewsLeft": 5,
        "timeInSeconds": 120
    }
]
````

# To get the user of known uuid, send a GET request to localhost:8080/api/user/{uuid}

The server will return the Username, email and the posts by the user
````
{
    "username": "test",
    "email": "test@testing.com",
    "codeList": [
        {
          "body": "Creating a post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
          "dateTime": "2021-01-16 10:41:16",
          "viewsLeft": 5,
          "timeInSeconds": 120
        },
        {
          "body": "Creating another post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
          "dateTime": "2021-01-16 10:41:17",
          "viewsLeft": 5,
          "timeInSeconds": 120
      },
      {
          "body": "Creating third post for the user of UUID: 6cd916ef-7e33-4fa5-b31b-fa94f0d32a3d",
          "dateTime": "2021-01-16 10:41:18",
          "viewsLeft": 5,
          "timeInSeconds": 120
      }
    ]
}
````


Added support for View Limit and Time Limit for code. Once the view limit or time limit is reached, the code will be deleted.
1. GET /code/{uuid}
1. GET /api/code/{uuid}
Above end points activate the checking method

# Usage front-end:
* Log In page: <br>
![Log In screen](https://user-images.githubusercontent.com/36816476/107867970-0b801e80-6ed4-11eb-8b56-6b62cfc72c21.PNG)


* Sign Up page: <br>
![Sign Up Screen](https://user-images.githubusercontent.com/36816476/107867986-310d2800-6ed4-11eb-89bc-9147dbc7d7a1.PNG)

* Filling details in sign up page: <br>
![Filling details in sign up screen](https://user-images.githubusercontent.com/36816476/107868003-5732c800-6ed4-11eb-931e-c0cc23b7e257.PNG)

* Home page after signing in: <br>
![Home page after signing in](https://user-images.githubusercontent.com/36816476/107868012-703b7900-6ed4-11eb-94ca-ce574821a447.PNG)

* Providing code and setting time and view limit: <br>
![Providing code and setting time and view limit](https://user-images.githubusercontent.com/36816476/107868022-952fec00-6ed4-11eb-9dc8-e7e87567c4e9.PNG)

* The code which we had provided appears in 'last' section
![The code which we had provided](https://user-images.githubusercontent.com/36816476/107868036-bb558c00-6ed4-11eb-9783-e2992ef85a66.PNG)

* Latest code by going to the 'latest' endpoint
![Latest code by going to the 'latest' endpoint](https://user-images.githubusercontent.com/36816476/107868039-c8727b00-6ed4-11eb-88be-74dcea842a5b.PNG)


