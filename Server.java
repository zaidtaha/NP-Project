import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;
public class Server {
    public static void main(String[] args) throws Exception{

        // Connect all 2-4 players

        ServerSocket server = new ServerSocket(8008);
        int count = 0;
        Join game = new Join();

        try {
            System.out.println("server online test");
            for(int i = 0 ; i < 2 ; i++){
                count++;
                Socket ServerSocket = server.accept();
                ThreadTest client = new ThreadTest(ServerSocket, count, game);
                client.start();
                game.connected++;
                System.out.println("new client " + count);
            }
            System.out.println("party complete");
        }
        finally{
        }

        while(game.connected!=game.ready) {
            Thread.sleep(500);
        }
        System.out.println("everyone is ready");
        game.DeckCreation();

        while(!game.end){
            Thread.sleep(500);
        }
    }
}

