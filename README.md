# CodeShare

A website to share code made using Spring Boot. It is still in development.

Rest end points available as of today:
1. GET      /code/new
1. POST     /code/new
1. GET      /code/{uuid}
1. GET      /code/latest
1. GET      /code/last/{count}

Rest end points for api:
1. POST     /api/code/new
1. GET      /api/code/{uuid}
1. GET      /api/code/latest
1. GET      /api/code/last/{count}
1. POST     /api/code/addAll
1. DELETE   /api/code/{uuid}

Added support for View Limit and Time Limit for code. Once the view limit or time limit is reached, the code will be deleted.
1. GET /code/{uuid}
1. GET /api/code/{uuid}
Above end points activate the checking method

