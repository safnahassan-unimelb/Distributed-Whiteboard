# COMP90015 Distributed Systems Assignment 2: Distributed Shared Whiteboard

### Student Name: Safna Hassan
### Student ID: 1144831

This project consists of the design and implementation of a Distributed Shared Whiteboard. The whiteboard has the capability to be shared between multiple users over a network, with varying access controls. The Graphical User Interface (GUI) allows users to concurrently draw shapes and add text on the board. It also has an embedded chat box for users to communicate over an active session. The creator of a board has additional access to a Menu Bar with drop-down menus for File Options (‘New’, ‘Open’, ‘Save’, and ‘Save as’) and Manager Options (“Kick Out User” and “Close Whiteboard”).

#### Startup model to run the application:

Manager creates a whiteboard by running:
```
java -jar create_whiteboard_jar.jar <serverIPAddress> <serverPort> <username>
```

Other users join a whiteboard by running:
```
java -jar join_whiteboard_jar.jar <serverIPAddress> <serverPort> <username>
```
