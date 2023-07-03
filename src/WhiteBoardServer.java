import shapes.*;
import shapes.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author Safna Hassan (1144831)
 */
public class WhiteBoardServer extends JFrame {

    private static final long serialVersionUID = -5698965795968512940L;
    private JTextArea chatArea;
    private JTextField chatBox;
    private JButton sendMessage;
    // buttons for drawing shapes
    private JButton lineButton, circleButton, rectangleButton, ovalButton, textButton;
    private JMenuItem newBoard = new JMenuItem("New WhiteBoard");
    private JMenuItem openBoard = new JMenuItem("Open WhiteBoard");
    private JMenuItem saveBoard = new JMenuItem("Save");
    private JMenuItem saveAs = new JMenuItem("Save As");
    private JMenuItem kickOut = new JMenuItem("Kick Out User");
    private JMenuItem closeBoard = new JMenuItem("Exit");

    private JButton colourPickerButton;

    private DrawingArea drawingArea;
    private String selectedShapeType = "line";
    private Color selectedColour = Color.BLACK;

//    File selected to open in Open Board Option
    private File openedBoard = null;
    // coordinates recorded for drawing shapes
    private ArrayList<Coordinate> coordinates = new ArrayList<>();

    // track the number of clients
    private int userUniqueId = 1;
    private JTextArea userList;
    // username of the server
    private String serverUsername;
    // port of the server
    private int port;
    // track the status of server
    private boolean serverOn = true;

    // store the clients connected
    private ArrayList<ClientThread> clients = new ArrayList<>();

    public WhiteBoardServer(int port, String serverUsername) {
        this.serverUsername = serverUsername;
        this.port = port;

        // initialise the server frame
        setTitle("Server WhiteBoard of " + serverUsername);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1000,700);
//        setResizable(false);
        getContentPane().setBackground(Color.DARK_GRAY);

        // initialise shape buttons
        Panel shapeTools = new Panel();
        shapeTools.setBounds(0,0,600,70);
        this.add(shapeTools);
        lineButton = new JButton("Line");
        lineButton.addActionListener(new ButtonEventListener());
        shapeTools.add(lineButton);
        circleButton = new JButton("Circle");
        circleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(circleButton);
        rectangleButton = new JButton("Rectangle");
        rectangleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(rectangleButton);
        ovalButton = new JButton("Oval");
        ovalButton.addActionListener(new ButtonEventListener());
        shapeTools.add(ovalButton);
        textButton = new JButton("Text");
        textButton.addActionListener(new ButtonEventListener());
        shapeTools.add(textButton);
        colourPickerButton = new JButton("Select Colour");
        colourPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(shapeTools, "Choose a colour", Color.BLACK);
                if (color != null) {
                    selectedColour = color;
                }
            }
        }); 
        shapeTools.add(colourPickerButton);

        // initialise the drawing area
        drawingArea = new DrawingArea();
        drawingArea.setBounds(0,70, 600, 500);
        this.add(drawingArea);
        drawingArea.addMouseListener(new MouseEventListener());

        // Create a Chat Window
        chatArea = new JTextArea();
        chatArea.setBackground(Color.BLACK);
        chatArea.setBounds(600,0,200,500);
        this.add(chatArea);

        chatBox = new JTextField();
        chatBox.setBounds(600,500,200,70);
        chatArea.append("Chats: \n");
        chatArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));
        chatBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,10));
        chatArea.setEnabled(false);
        this.add(chatBox);
        sendMessage = new JButton("Send");
        sendMessage.setBounds(700, 570, 100, 40);
        sendMessage.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,10));
        sendMessage.addActionListener(new ButtonEventListener());
        this.add(sendMessage);

//        Create List of Users for Display
        userList = new JTextArea();
        userList.setBackground(Color.WHITE);
        userList.setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));
        userList.setBorder(BorderFactory.createLineBorder(Color.GREEN,10));
        userList.setBounds(800,0,100,250);
        this.add(userList);

//        Menu Bar for Manager
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        newBoard.addActionListener(new MenuActionListener());
        openBoard.addActionListener(new MenuActionListener());
        saveBoard.addActionListener(new MenuActionListener());
        saveAs.addActionListener(new MenuActionListener());
        fileMenu.add(newBoard);
        fileMenu.add(openBoard);
        fileMenu.add(saveBoard);
        fileMenu.add(saveAs);


        JMenu managerOptions = new JMenu(("Manager Options"));
        menuBar.add(managerOptions);
        closeBoard.addActionListener(new MenuActionListener());
        kickOut.addActionListener(new MenuActionListener());
        managerOptions.add(kickOut);
        managerOptions.add(closeBoard);




        setLocationRelativeTo(null);
        updateUserList();
        new ServerThread().start();
    }

//    Server Thread Implementation
    private class ServerThread extends Thread {
        @Override
        public void run() {

            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.setReuseAddress(true);
                while (serverOn) {
//                    Thread-per-client-request: Create a new thread object for each client
                    Socket clientSocket = serverSocket.accept();
                    ClientThread client = new ClientThread(clientSocket);
                    client.start();
                }
            } catch (IOException e) {
                System.out.println("error: server starting failed");
                System.exit(0);
            }

        }
    }

    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            coordinates.clear();
//           Set Shape Selected by user
            if (e.getSource() == lineButton) {
                selectedShapeType = "line";
            } else if (e.getSource() == circleButton) {
                selectedShapeType = "circle";
            } else if (e.getSource() == rectangleButton) {
                selectedShapeType = "rectangle";
            } else if (e.getSource()== ovalButton) {
                selectedShapeType = "oval";
            } else if (e.getSource() == textButton) {
                selectedShapeType = "text";
            } else if (e.getSource() == sendMessage) {
                String chat = chatBox.getText();
                String chatText = "\n" + serverUsername +": " + chat;
                chatArea.append(chatText);
                chatBox.setText("");
                for (ClientThread client : clients) {
                    try {
                        client.out.writeObject("chat");
                        client.out.writeObject(chatText);
                    } catch (IOException error) {
                        System.out.println("Error: Couldn't " +
                                "broadcast chat to client whiteboards.");
                    }
                }
            }
        }
    }

    private class MenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == newBoard) {
                newDrawingArea();
            } else if (e.getSource()== openBoard) {
                open();
            } else if (e.getSource() == saveBoard) {
                saveImage();
            } else if (e.getSource()==saveAs) {
                saveAsImage();
            } else if (e.getSource()== closeBoard) {
                serverOn = false;
                System.exit(0);
            } else if (e.getSource()==kickOut) {
                String clientName = JOptionPane.showInputDialog("Type the name of user to kick the user out");
                if (clientName != null) {
                    ClientThread client = searchClient(clientName);
                    if (client != null) {
                        try {
                            client.out.writeObject("kick");
                        } catch (IOException e1) {
                            System.out.println("error: kicking the client failed");
                        }

                        removeClient(client);
                    } else {
                        JOptionPane.showMessageDialog(null, "No user found");
                    }
                }

            }
        }
    }

//Method used for searching current active users by username.
    private ClientThread searchClient(String clientName) {
        for (ClientThread client : clients) {
            if (client.clientUsername.equals(clientName)) {
                return client;
            }
        }
        return null;
    }

//    Shapes are drawn using mouse clicks.
//    Hence, MouseEventListener method has been implemented to
//    manage the coorindates provided by users
    private class MouseEventListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            super.mouseClicked(e);
            coordinates.add(new Coordinate(e.getX(), e.getY()));

            ShapeDetails shape = null;

            switch (selectedShapeType) {
                case "line":
                    if (coordinates.size() == 2) {
                        shape = new Line(coordinates.get(0),
                                coordinates.get(1), selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "circle":
                    if (coordinates.size() == 2) {
                        shape = new Circle(coordinates.get(0),
                                coordinates.get(1), selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "rectangle":
                    if (coordinates.size() == 2) {
                        shape = new Rectangle(coordinates.get(0),
                                coordinates.get(1), selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "oval":
                    if (coordinates.size() == 2) {
                        shape = new Oval(coordinates.get(0),
                                coordinates.get(1), selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "text":
                    if (coordinates.size() == 1) {
                        String text = JOptionPane.
                                showInputDialog("Enter some text:");
                        if (!text.isEmpty()) {
                            shape = new Text(coordinates.get(0), text, selectedColour);
                            coordinates.clear();
                        }
                    }
                    break;
            }

            if (shape != null) {
                addShape(shape);
            }
        }
    }

    // add shape drawn and propagated to other users
    public synchronized void addShape(ShapeDetails shape) {
        drawingArea.drawShape(shape);
        for (ClientThread client : clients) {
            try {
                client.out.writeObject(shape.getShapeType());
                client.out.writeObject(shape);
            } catch (IOException e) {
                System.out.println("Error: Couldn't " +
                        "broadcast shape on client whiteboards.");
            }
        }
    }

    // reset the drawing area
    public synchronized void newDrawingArea() {
        drawingArea.newDrawingArea();
        openedBoard = null;

        for (ClientThread client : clients) {
            try {
                client.out.writeObject("new");
            } catch (IOException e) {
                System.out.println("Error: Couldn't broadcast CLEARSCREEN on client whiteboards.");
            }
        }
    }

    // add the client and update user list
    public synchronized void addClient(ClientThread client) {
        clients.add(client);
        updateUserList();
    }

    // remove the client and update user list
    public synchronized void removeClient(ClientThread client) {
        clients.remove(client);
        updateUserList();
    }

    // update the user list when there's changes to client list
    private void updateUserList() {
        StringBuilder list = new StringBuilder("Active Users:\n"+serverUsername + "\n");
        for (ClientThread client : clients) {
            list.append(client.clientUsername).append("\n");
        }
        userList.setText(list.toString());
        for (ClientThread client : clients) {
            try {
                client.out.writeObject("userlist");
                client.out.writeObject(list.toString());
            } catch (IOException e) {
                System.out.println("Error: Couldn't " +
                        "broadcast the list of users.");
            }
        }
    }

    // update the whiteboard of client thats newly joined
    public synchronized void updateDrawingBoardOfNewClient(ClientThread client) {
        if (drawingArea.getBackgroundImage() != null) {
            try {
                client.out.writeObject("open");
                client.out.writeObject(openedBoard);
            } catch (IOException e) {
                System.out.println("Error: Failure in broadcasting update.");
            }

        }
        for (ShapeDetails shape : drawingArea.getShapes()) {
            try {
                client.out.writeObject(shape.getShapeType());
                client.out.writeObject(shape);
            } catch (IOException e) {
                System.out.println("Error: Failure in broadcasting update.");
            }
        }
    }

//    Saves current whiteboard to whiteboard.png by default
    public void saveImage() {
        BufferedImage image = new BufferedImage(drawingArea.getWidth(),drawingArea.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        drawingArea.printAll(graphics2D);
        graphics2D.dispose();
        String path = "C:\\Users\\safna\\Downloads\\whiteboard.png";
        try {
            ImageIO.write(image,"png",new File(path));
            JOptionPane.showMessageDialog(null, "Image saved to "+path);
        } catch (IOException e) {
            System.out.println("Error: Could not perform Save operation.");
        }
    }

    public void saveAsImage() {
        BufferedImage image = new BufferedImage(drawingArea.getWidth(),drawingArea.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        drawingArea.printAll(graphics2D);
        graphics2D.dispose();
        try {
            // set default path to Downloads folder.
            String path = "C:\\Users\\safna\\Downloads\\";
            String filename = JOptionPane.
                    showInputDialog("Enter File Name:");
            if (!filename.isEmpty()) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("C:\\Users\\safna\\Downloads\\"));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    path = fileChooser.getSelectedFile().getPath();
                }
                ImageIO.write(image,"png",new File(path+"\\"+filename+".png"));

            }
            JOptionPane.showMessageDialog(null, "Image saved to "+path);

        } catch (IOException e) {
            System.out.println("Error: Could not perform Save As operation.");
        }
    }

    public synchronized void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\safna\\Downloads\\"));
//        Allow only png files to be opened
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG IMAGES","png");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            openedBoard = selectedFile;
            try {
                drawingArea.openImage(selectedFile);
                for (ClientThread client : clients) {
                    try {
                        client.out.writeObject("open");
                        client.out.writeObject(selectedFile);
                    } catch (IOException e) {
                        System.out.println(e);
                        System.out.println("Error: Couldn't open selected whiteboard image on client whiteboards.");
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: Could not open selected image.");
            }
        }
    }
//    Client Thread Implementation
//    Thread per client architecture
    private class ClientThread extends Thread {
        Socket socket;
        int clientId;
        String clientUsername;

        ObjectInputStream in;
        ObjectOutputStream out;


        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            clientId = userUniqueId++;
        }

        @Override
        public void run() {
            try {
                while (serverOn) {

                    String instruction = in.readObject().toString();
                    // if instruction is "join"
                    if (instruction.equals("join")) {
                        // Allow a user to join the board, or deny access
                        clientUsername = in.readObject().toString()
                                + "-"+clientId;
                        int confirmationFromServer = JOptionPane.
                                showConfirmDialog(null,
                                "Allow " + clientUsername+ " to join the Whiteboard?",
                                "Potential client found..",
                                JOptionPane.OK_CANCEL_OPTION);

                        if (confirmationFromServer == JOptionPane.OK_OPTION) {
                            addClient(this);
                            out.writeObject("join");
                            out.writeObject(clientUsername);
                            updateDrawingBoardOfNewClient(this);

                        } else {
                            out.writeObject("join");
                            out.writeObject("no");
                        }

                    } else if (instruction.equals("chat")) {
                        String chat = (String) in.readObject();
                        chatArea.append("\n" + clientUsername +": " + chat);
                    } else {
                        ShapeDetails shape = (ShapeDetails)
                                in.readObject();
                        addShape(shape);
                    }

                }

            } catch (Exception  e) {
                removeClient(this);
            }
        }
    }
}
