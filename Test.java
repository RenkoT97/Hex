import java.util.Scanner;
import logic.HexLogic;

public class Test {
    public static void main (String[] args) { 
        HexLogic l = new HexLogic(8);
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        while (true) {   
            System.out.println("Enter username");
            String[] userName = myObj.nextLine().split(" ");
            HexLogic.fieldType a = (userName[0].equals("1")) ? HexLogic.fieldType.TYPE1 : HexLogic.fieldType.TYPE2;
            l.setField(a, Integer.parseInt(userName[1]), Integer.parseInt(userName[2]));
            l.repr(a);
            if (l.hasWon(a)) {
                System.out.println(a.name());
                break;
            }
        }
        myObj.close();
        
    }
}