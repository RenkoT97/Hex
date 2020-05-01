import java.util.Scanner;
import logic.HexLogic;
import logic.FieldType;

public class Test {
    public static void main (String[] args) { 
        HexLogic l = new HexLogic(8);
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        while (true) {   
            System.out.println("Enter username");
            String[] userName = myObj.nextLine().split(" ");
            boolean player = userName[0].equals("1");
            l.makeMove(
                player, 
                Integer.parseInt(userName[1]), 
                Integer.parseInt(userName[2])
            );
            if (l.hasWon(player)) {
                System.out.println((player) ? "player1" : "player2");
                break;
            }
        }
        myObj.close();
    }
}