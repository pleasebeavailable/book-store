Online book store application.

I decided to have 3 models: Book, User and Order

Book model represents books which are available, and there are id, name, type and price values
User which represents buyer, and there are id, username and loyaltyPoints values
Order which represents transaction. 
Important data are: listOfBooks, withLoyaltyPoints, username and basePrice

I decided to write this project using functional endpoints with Project Reactor framework.
Biggest focus on writing lamda expressions and following separation of concerns.
Because there is no database I created mock_data folder.

To test this app, one should run executeOrder test in OrderHandlerTest.java file.
And result is logged in console.
