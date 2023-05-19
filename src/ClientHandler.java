import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
  private final Socket clientSocket;
  private final Server server;
  private PrintWriter out;

  public ClientHandler(Socket socket, Server server) {
    this.clientSocket = socket;
    this.server = server;
  }

  public void sendMessage(String message) {
    out.println(message);
  }

  public void run() {
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      out.println("Enter your username: ");
      String username = in.readLine();

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        System.out.println("Received message: " + inputLine);
        server.broadcastMessage(username + ": " + inputLine, this);
      }

      out.close();
      in.close();
      clientSocket.close();
      server.removeClient(this);

      System.out.println("Connection to client closed.");
    } catch (IOException e) {
      System.out.println("Connection to client lost.");
    }
  }
}
