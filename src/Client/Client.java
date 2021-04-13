package Client;
import Protocol.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class implements a simple client
 *
 * Laboratoire Protocol
 *
 * @date   11.04.2021
 * @author Christian Gomes & Johann Werkle
 *
 */

public class Client {

    private static final int PORT = 4000;
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    boolean isConnected = false;


    public static void main(String[] args) {

        try {
            new Client().connect("localhost", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class Listener implements Runnable {

        @Override
        public void run() {

            String response = "";
            try {
                while ((isConnected && (response = in.readLine()) != null)) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                isConnected = false;
            }
        }
    }


    public void connect(String serverAdress, int portServer) throws IOException {

        //Connection on Server
        try{
            //Localhost dans le cas du laboratoire
            clientSocket = new Socket(serverAdress,portServer);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            isConnected = true;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Listener()).start();



        calculate();

    }

    public void calculate() throws IOException {

        //Read Input User
        BufferedReader standardInput =new BufferedReader(new InputStreamReader(System.in));
        String line;
        out.println(Protocol.CMD_START);
        out.flush();
        while((line = standardInput.readLine()) != Protocol.CMD_QUIT){

            out.println(line);
            out.flush();
        }

        disconnect();

    }

    public void disconnect() {
        isConnected = false;
    }

}
