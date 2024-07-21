# Messaging System

> Program consists of a server socket to which multiple clients can connect to

- Note : First run server main then you can instantiate clients

> Multiple clients can connect to the server | a server socket
> Clients can message to server, and notify everyone else in the server with the message they sent
> Also all clients notified, if a client leaves (disconnects) from server

> Clients can send messages to entire server or to selected clients connected to the server socket

> In progress of adding option where user can choose from multiple servers to connect to, 
>* A total of 10 people can be in one server
>* Users can also switch from light mode to dark mode with button click in chat room
>* Potential custom messaging system implementation

> On macOS systems, when a client disconnects, there is a bug where newly connected users to the server
> will receive a "Client has disconnected" message. Also, the Json reading and writing component glitches
> where the capacity is one more than it should be, especially when testing adding one client to a server than
> disconnecting, in json file it will still say that there is a client in that server.