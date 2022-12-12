# Capstone-storage



```
Storify is a storage management system created with an fully java backend and react frontend.
You can add items with EAN code, create transport orders for storage and retrieval that can be confirmed.
```
## `clone:`

FRONTEND:

clone repo => cd frontend => npm i => npm start

BACKEND:

clone repo => start the BackendApplication.java

MISC:

mongoDB database runs on localhost:27017.

For deploy you have to set the MONGO_DB_URI with command flyctl secrets set MONGO_DB_URI= your_uri.

and the little file `fly.toml` that's already included.

```
If you want to use the feature to add a new item by ean you need an api token from 'https://www.ean-search.org/'.
'But it is not for free!'.
You have to set the token in your application.properties:
Example:
'token:12345678'.
```
If you don't want to use the the feature you can also use the test mode button on the mainpage for a test condition.
