import java.net.Socket;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = null;
        int counter = 0;
        try {
            socket = new Socket("localhost", 8008);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out);

            int c;
            String ready_question = "";
            while((c=in.read()) != '\n'){
                ready_question+=(char)c;
            }
            System.out.println(ready_question);

            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            writer.println(s);
            writer.flush();

            System.out.println("sent yes message");

            c = 0;
            String Game_beginning = "";
            while((c=in.read()) != '\n'){
                Game_beginning+=(char)c;
            }
            System.out.println(Game_beginning);
            // game is beginning message
            c = 0;
            String dist = "";
            while((c=in.read()) != '\n'){
                dist+=(char)c;
            }
            System.out.println(dist);
            // Cards have been distributed message

            boolean end = false;
            while(!end) {

                c = 0;
                String checker = "";
                while ((c = in.read()) != '\n')
                    checker += (char) c;

                if(checker.contains("You lost")==true){
                    System.out.println("You lost"); break;
                }
                else if(checker.contains("Your turn")==true)
                    System.out.println("Your turn");

                else{
                    System.out.println("You won"); break;
                }

                c = 0;
                String message = "";
                while ((c = in.read()) != '\n') {
                    if (c == '.')
                        c = '\n';
                    message += (char) c;
                }
                System.out.println(message);

                c = 0;
                String options = "";
                while ((c = in.read()) != '\n') {
                    options += (char) c;
                }
                System.out.println(options);

                boolean valid = false;
                while(!valid) {
                    int choice = scanner.nextInt();
                    writer.println(choice);
                    writer.flush();

                    c = 0;
                    String option_checker = "";
                    while ((c = in.read()) != '\n')
                        option_checker += (char) c;
                    if(option_checker.contains("proceed")){
                        valid = true;
                    }
                    else{
                        System.out.println("Option is not valid, choose again");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            System.out.println("Thanks for playing!");
            if(socket != null){
                socket.close();
            }
        }
    }
}