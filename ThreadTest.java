import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ThreadTest extends Thread {
    Socket ServerSocket;
    int clientNo;
    Join game;
    ThreadTest(Socket inSocket, int ClientNo, Join g){
        ServerSocket = inSocket;
        clientNo = ClientNo;
        game = g;
    }

    public void send_message(String s, PrintWriter writer){
        writer.println(s);
        writer.flush();
    }

    public String receive_message(InputStream in) {
        try {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                if (c == '\n') {
                    break;
                }
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void print_Hand(ArrayList<Card> H, PrintWriter writer) {
        String s = "";
        for(int i = 0 ; i < H.size() ; i++){
            s += (i+1) + "- " + H.get(i).toString() + ".";
        }
        s += "Table card is " + game.table.toString() + ".";
        send_message(s,writer);
    }

    public ArrayList<Integer> print_options(ArrayList<Card> H, PrintWriter writer){
        ArrayList<Integer> a = new ArrayList<Integer>();
        String options = "Your options are: ";
        boolean opt = false;
        for(int i = 0 ; i < H.size() ; i++){
            if(H.get(i).toString().contains("fun") && H.get(i).color == Card.Color.__){
                options = options + (i+1) + " ";
                a.add(i+1);
                opt = true;
            }
            else if(H.get(i).color == game.table.color){
                options = options + (i+1) + " ";
                a.add(i+1);
                opt = true;
            }
            else if(H.get(i).toString().contains("fun") && game.table.toString().contains("fun")){
                if((H.get(i).toString().contains("PLUSTWO") && game.table.toString().contains("PLUSTWO"))
                        || (H.get(i).toString().contains("SUBWAY") && game.table.toString().contains("PLUSTWO"))
                        || (H.get(i).toString().contains("SKIP") && game.table.toString().contains("SKIP"))){
                    options = options + (i+1) + " ";
                    a.add(i+1);
                    opt = true;
                }
            }
            else {
                for(int j = 0 ; j < 10 ; j++){
                    if(H.get(i).toString().contains(Integer.toString(j)) && game.table.toString().contains(Integer.toString(j))){
                        options = options + (i+1) + " ";
                        a.add(i+1);
                        opt = true;
                    }
                }
            }
        }
        if(opt == false){
            options += "draw a card (type -1)";
            a.add(-1);
        }
        send_message(options, writer);
        return a;
    }

    public void run(){
        try{
                InputStream in = ServerSocket.getInputStream();
                OutputStream out = ServerSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(out);

                while(game.connected==1) {
                    Thread.sleep(500);
                }

                if(game.connected>1) {
                    send_message("Are you ready to begin?", writer);

                    String s = receive_message(in);
                    System.out.println(s);

                    if(s.compareTo("yes"+s.charAt(3)) == 0){
                        System.out.println(clientNo + " is ready");
                        game.ready++;
                    }
                    // no validation to check other messages or renter
                }

                while(game.connected!=game.ready) {
                    Thread.sleep(500);
                }
                send_message("Game is beginning",writer);

                while(!game.Deck_distributed){
                    Thread.sleep(500);
                }
                send_message("Cards are being distributed", writer);
                while(clientNo != game.current_dist){
                    Thread.sleep(500);
                }
                ArrayList<Card> Hand = new ArrayList<Card>();
                for(int i = 0 ; i < 7 ; i++){
                    Hand.add(game.deck.drawACard());
                }
                game.current_dist++;

                //send_message(Integer.toString(Hand.size()),writer);


                while(!game.end) {
                    // One round

                    while (game.turn != clientNo) {
                        Thread.sleep(500);
                    }

                    if(game.end){
                        send_message("You lost", writer);
                        break;
                    }
                    else{
                        send_message("Your turn", writer);
                    }
                    boolean valid = false;
                    print_Hand(Hand, writer);
                    ArrayList<Integer> options;
                    options = print_options(Hand, writer);
                    while(!valid) {
                        String choice_s = (receive_message(in));
                        int choice = Integer.parseInt(choice_s.substring(0, choice_s.length() - 1));
                        if (options.contains(choice))
                            valid = true;
                        else {
                            send_message("options is not valid", writer);
                            continue;
                        }
                        send_message("proceed", writer);
                        if (choice != -1) {
                            System.out.println("Player choice is: " + choice);
                            game.play(Hand.get(choice - 1));
                            Hand.remove(choice - 1);
                        } else {
                            Hand.add(game.deck.drawACard());
                            game.play(game.table);
                            System.out.println("Player: " + clientNo + " drew a card");
                        }
                    }
                /*
                if(Hand.size()==1)
                    game.UNO();
                */
                    if (Hand.size() == 0) {
                        game.end = true;
                        System.out.println("Player " + clientNo + " won!");
                        send_message("You won",writer);
                    }
                }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            if(ServerSocket!=null)
                ServerSocket.close();
        }
        catch(IOException ex){

        }
    }
}