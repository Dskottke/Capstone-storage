# Capstone-storage



```
Storify is a warehouse management system built with a full Java backend and React frontend.
You can add items with EAN code, create transfer orders for put-away and take-away that can be confirmed.
```
## `clone:`

FRONTEND:

clone repo => cd frontend => npm i => npm start

BACKEND:

clone repo => start the BackendApplication.java

MISC:

mongoDB database runs on localhost:27017.

For deploy you have to set the MONGO_DB_URI with command flyctl secrets set MONGO_DB_URI= your_uri.

and the file `fly.toml` that's already included.

```
If you want to use the function to add a new item by ean, you need an api token from 'https://www.ean-search.org/'.
But it's not free!'.
You need to set the token in your application.properties:
Example:
'token:12345678
```
If you do not want to use the function, you can also use the test Mode button on the main page for a simulation state.
