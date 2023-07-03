import java.io.IOException;
import java.net.Socket;
/**
 * @author Safna Hassan (1144831)
 */
public class JoinWhiteBoard {

    public static void main(String[] args) {
//		Data validation check
        if (args.length != 3) {
            System.out.println("Correct instruction: java joinWhiteBoard <serverIPAddress> <serverPort> <username>");
        } else {
            try {
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                WhiteBoardClient client = new WhiteBoardClient(socket, args[2]);
                client.setVisible(true);
            } catch (IOException e) {
                System.out.println("ERROR: Failed to connect to server.");
            }
        }
    }
}
