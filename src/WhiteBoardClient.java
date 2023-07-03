import shapes.*;
import shapes.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author Safna Hassan (1144831)
 */
public class WhiteBoardClient extends JFrame {

    private static final long serialVersionUID = 3722454170431575687L;

    private JTextArea chatArea;
    private JTextField chatBox;
    private JButton sendMessage;
    // show list of users on left side
    private JTextArea userList;
    // buttons for drawing shapes
    private JButton lineButton, circleButton, rectangleButton, ovalButton, textButton;

    private DrawingArea drawingArea;
    // shape selected to draw on whiteboard
    private String selectedShapeType;
    private JButton colourPickerButton;
    private Color selectedColour = Color.BLACK;
    // coordinates recorded for drawing shapes
    private ArrayList<Coordinate> coordinates = new ArrayList<>();

    // username of the client
    private String clientUsername;
    private Socket socket;
    // writing and reading object data for server
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public WhiteBoardClient(Socket clientSocket, String clientUsername)
            throws IOException {
        this.clientUsername = clientUsername;
        this.socket = clientSocket;

        // initialise the writing and reading streams
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        // initialise JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(920,800);
        getContentPane().setBackground(Color.PINK);

        // initialise drawing area
        drawingArea = new DrawingArea();
        drawingArea.setBounds(0,70, 600, 500);
        this.add(drawingArea);
        drawingArea.addMouseListener(new MouseEventListener());

        // initialise shape buttons
        Panel shapeTools = new Panel();
        shapeTools.setBounds(0,0,600,70);
        this.add(shapeTools);
        lineButton = new JButton("Draw line");
        lineButton.addActionListener(new ButtonEventListener());
        shapeTools.add(lineButton);
        circleButton = new JButton("Draw circle");
        circleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(circleButton);
        rectangleButton = new JButton("Draw rectangle");
        rectangleButton.addActionListener(new ButtonEventListener());
        shapeTools.add(rectangleButton);
        ovalButton = new JButton("Oval");
        ovalButton.addActionListener(new ButtonEventListener());
        shapeTools.add(ovalButton);
        textButton = new JButton("Write text");
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

        // Create a Chat Window
        Panel chatPanel = new Panel();
        chatPanel.setBounds(600,0,200,800);
//        this.add(chatPanel);
        chatArea = new JTextArea();
        chatArea.setBackground(Color.BLACK);
        chatArea.setBounds(600,0,200,500);
        this.add(chatArea);

        chatBox = new JTextField();
        chatBox.setBounds(600,500,200,70);
        chatArea.append("Chats: \n");
        chatArea.setBorder(BorderFactory.createLineBorder(Color.PINK, 10));
        chatBox.setBorder(BorderFactory.createLineBorder(Color.PINK,10));
        chatArea.setEnabled(false);
        this.add(chatBox);
        sendMessage = new JButton("Send");
        sendMessage.setBounds(700, 570, 100, 40);
        sendMessage.setBorder(BorderFactory.createLineBorder(Color.PINK,10));
        sendMessage.addActionListener(new ButtonEventListener());
        this.add(sendMessage);
        //        Create List of Users for Display
        userList = new JTextArea();
        userList.setBackground(Color.WHITE);
        userList.setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));
        userList.setBorder(BorderFactory.createLineBorder(Color.GREEN,10));
        userList.setBounds(800,0,100,250);
        this.add(userList);

        // default selected shape is line
        selectedShapeType = "line";
        setLocationRelativeTo(null);

        // sends request to server for joining
        out.writeObject("join");
        out.writeObject(clientUsername);
        new Client().start();

    }

    // Client extends Thread that runs until server broke
    // or kicked
    private class Client extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    // read instruction from server
                    String instruction = in.readObject().toString();
                    switch (instruction) {
                        // if instruction is "join"
                        case "join":
                            String reply = in.readObject().toString();
                            // Exit the client if server replies no
                            if (reply.equals("no")) {
                                System.out.println("Error: " +
                                        "Access denied to join the whiteboard.");
                                System.exit(0);
                            // Set the title of JFrame
                            } else {
                                setTitle("Whiteboard for "
                                        + clientUsername);
                            }
                            break;
                        case "userlist":
                            String list = in.readObject().toString();
                            userList.setText(list);
                            break;
//                            Manager opens a new whiteboard
                        case "new":
                            drawingArea.newDrawingArea();
                            break;
//                            Manager opens a saved whiteboard
                        case "open":
                            File selectedFile = (File) in.readObject();
                            drawingArea.openImage(selectedFile);
                            repaint();
                            break;
                        // if instruction is "kick"
                        case "kick":
                            // Exit the client system and notify the client
                            JOptionPane.showMessageDialog(null, "You have been removed from this White Board.");
                            System.exit(0);
                        // else, which the instruction is shapes
                        case "chat":
                            String chat = (String) in.readObject();
                            chatArea.append(chat);
                            break;
                        default:
                            // draw the shape on whiteboard
                            ShapeDetails shape = (ShapeDetails)
                                    in.readObject();
                            drawingArea.drawShape(shape);
                            break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "server is not available now.");
                System.exit(0);
            }
        }
    }

    // Action listeners for shape buttons to determine
    // the shape that is going to be drawn
    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            coordinates.clear();
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
            }else if (e.getSource() == sendMessage) {
                String chat = chatBox.getText();
                chatArea.append("\n" + clientUsername +": " + chat);
                chatBox.setText("");
                try {
                    out.writeObject("chat");
                    out.writeObject(chat);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " +
                            "Couldn't connect to Server Chat");
                }
            }
        }
    }

//    Eventlistener to draw on whiteboard, after user selects coordinates
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
                                coordinates.get(1),selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "circle":
                    if (coordinates.size() == 2) {
                        shape = new Circle(coordinates.get(0),
                                coordinates.get(1),selectedColour);
                        coordinates.clear();
                    }
                    break;
                case "rectangle":
                    if (coordinates.size() == 2) {
                        shape = new Rectangle(coordinates.get(0),
                                coordinates.get(1),selectedColour);
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
                                showInputDialog("Your shapes.Text:");
                        if (!text.isEmpty()) {
                            shape = new Text(coordinates.get(0), text,selectedColour);
                            coordinates.clear();
                        }
                    }
                    break;
            }

            if (shape != null) {
                try {
                    out.writeObject(shape.getShapeType());
                    out.writeObject(shape);
                } catch (IOException er) {
                    JOptionPane.showMessageDialog(null, "error: " +
                            "failed to connect to server");
                }
            }
        }
    }
}
