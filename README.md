# CodeShare

A website to share code made using Spring Boot. It is still in development.
Open for contributions.

# Features that I want to implement:
- [X] Swagger for API docs
- [X] Deleting all posts of a user if a user is deleted
- [X] Setting time limit so that code will be deleted if time limit is reached
- [X] Setting view limit so that code will be deleted if view limit is reached
- [X] Hashing of password for security
- [ ] Implementing Authentication and Authorization

# Known issue:
1. localhost:8080/signin doesn't sign in the user.

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



