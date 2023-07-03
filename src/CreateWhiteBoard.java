/**
 * @author Safna Hassan (1144831)
 */
public class CreateWhiteBoard {

    public static void main(String[] args) {
		
//		Data validation check
        if(args.length != 3) {
            System.out.println("Correct instructions: java CreateWhiteBoard <serverIPAddress> <serverPort> username");
        } else {

            WhiteBoardServer server = new WhiteBoardServer(Integer.parseInt(args[1]), args[2]);
            server.setVisible(true);
        }
    }
}
