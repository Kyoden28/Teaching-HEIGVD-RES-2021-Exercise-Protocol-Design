Author : Johann Werkle

## What transport protocol do we use?

TCP, we don't need a really fast protocol but we obviously need a reliable way to transmit datas and establish a connection.

## How does the client find the server (addresses and ports)?

The client has to know the IP and port to contact the server.

## Who speaks first?

The client, by asking authorization to the server to connect.

## What is the sequence of messages exchanged by the client and the server? (flow)

| Client                 | Server                                     |
| ---------------------- | ------------------------------------------ |
| Asks for connection -> |                                            |
|                        | <- Grants or refuses connection            |
| Sends operation ->     |                                            |
|                        | <- Sends back either an answer or an error |
| Stop application ->    |                                            |
|                        | <- Closes connection                       |

## What happens when a message is received from the other party? (semantics)

The server checks the request and sends back an answer in the form of a result or an error, depending on the request's format.

## What is the syntax of the messages? How we generate and parse them? (syntax)

| Message                      | Description                                                  |
| ---------------------------- | ------------------------------------------------------------ |
| START                        | from client, asks for connection                             |
| WELCOME                      | from server, confirms connection                             |
| COMPUTE CODE  value1, value2 | from client, asks for the server to do the operation CODE with value1 and value2. Ex : COMPUTE ADD 1, 2 |
| RESULT value                 | from server, returns the result (if syntax is respected)     |
| ERROR id DESCRIPTION         | from server, returns an error, its identification value and a short description |
| QUIT                         | from client, asks to end application                         |

## Who closes the connection and when

The server, after an connection issue or after being asked by the client with a `QUIT` message.