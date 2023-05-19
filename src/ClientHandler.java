import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
  private final Socket clientSocket;

  public ClientHandler(Socket socket) {
    this.clientSocket = socket;
  }

  public void run() {
    try {
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      System.out.println("Client connected: " + clientSocket);

      String message;
      while ((message = in.readLine()) != null) {
        System.out.println("Client: " + message);
        out.println("Server received: " + message);
      }

      in.close();
      out.close();
      clientSocket.close();

      System.out.println("Connection to client closed.");
    } catch (IOException e) {
      System.out.println("Connection to client lost.");
    }
  }
}