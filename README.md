# StocksApp

Build an application simulate market participants who want to buy stocks and currency. Using the Yahoo Finance API get
the current market price. On top of the current value the system adds a spread and offers it to the client in real time.
 A client has an account and based on the funds available the order is fulfilled or not. All participants’ goods are
 tracked in accounts: securities are kept in a depot, cash is kept in a deposit. Each account is charged as soon
 as an order is placed to avoid overcommitment.

Version 2
Add the options of selling the stocks and the bids and asks are matched in an orderbook and a trade is made.
The engine should try and match the highest bids with the lowest asks as long as possible. If an order can’t be
fulfilled entirely it will be rejected.
