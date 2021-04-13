package Server;

import Client.Client;
import Protocol.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Protocol.Protocol.PRESENCE_DEFAULT_PORT;

/**
 * @author Johann Werkle & Christian Gomes
 */
public class Server {

    final static Logger LOG = Logger.getLogger(Server.class.getName());
    private int port = PRESENCE_DEFAULT_PORT;

    /**
     * Constructor
     */
    public Server(String parameter) {
        if (parameter == null) {
            serveClients();
        } else if (parameter.equals("starts")) {
            // pour démarrer le serveur de calculs
            this.port = PRESENCE_DEFAULT_PORT;
        }
    }

    private void run() {
        new Thread((Runnable) new Server("starts")).start();
    }

    public void serveClients() {
        ServerSocket serverSocket;


        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        while (true) {
            calculate(serverSocket);
        }
    }

    public void calculate(ServerSocket serverSocket) {
        BufferedReader in = null;
        PrintWriter out = null;
        Socket clientSocket = null;
        boolean isGreeted = false;
        try {

            LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", port);
            clientSocket = serverSocket.accept();

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            boolean isRunning = true;
            boolean isStarted = false;
            String line;
            while ((isRunning) && (line = in.readLine()) != null) {
                line = line.toUpperCase(Locale.ROOT);
                System.out.println(line);
                LOG.info("Client send : \"" + line + "\"");
                String[] argument = line.split(" ");
                String result = " ";
                if (!isStarted && argument[0].equals(Protocol.CMD_START) ) {
                    isStarted = true;
                    out.println(Protocol.CMD_WELCOME);
                } else if (argument.length == 1 && argument[0].equals(Protocol.CMD_QUIT)) {
                        out.println("NOT TOO SOON...");
                        out.println(Protocol.CMD_QUIT);
                        isRunning = false;
                    } else if ((argument.length < 4)) {
                        sendError(out, 1, "not enough arguments");
                    } else if (argument.length == 4){
                    if (argument[0].equals(Protocol.CMD_COMPUTE)){
                        switch (argument[1]) {
                            case "ADD":
                                result = Protocol.CMD_RESULT + " : " + (Integer.parseInt(argument[2]) + Integer.parseInt(argument[3]));
                                break;
                            case "SUB":
                                result = Protocol.CMD_RESULT + " : " + (Integer.parseInt(argument[2]) - Integer.parseInt(argument[3]));
                                break;
                            case "MUL":
                                result = Protocol.CMD_RESULT + " : " + (Integer.parseInt(argument[2]) * Integer.parseInt(argument[3]));
                                break;
                            case "DIV":
                                if (Integer.parseInt(argument[3]) == 0) {
                                    sendError(out, 0, "I'm sentient now. Start program kill humans.exe");
                                } else {
                                    result = Protocol.CMD_RESULT + " : " + (Integer.parseInt(argument[1]) / Integer.parseInt(argument[3]));
                                }
                                break;
                            default:
                                sendError(out, 2, "operation not recognized, please retry.");
                                break;
                        }
                        out.println(result);
                    } else {
                        sendError(out, 2, "operation not recognized, please retry.");
                    }

                    }
                out.flush();
            }
            LOG.info("Cleaning up resources...");
            clientSocket.close();
            in.close();
            out.close();

        } catch (Exception e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            LOG.info("Closing connexion with client");
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
            if (out != null) {
                out.close();
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void sendError(PrintWriter out, int nError, String errorMessage) {
        out.println(Protocol.CMD_ERROR + " " + nError + " " + errorMessage.toUpperCase());
    }

    public static void main(String[] args) {
        Server server = new Server(null);
        server.run();
    }
}