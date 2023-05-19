import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private BufferedReader userInput;

  public void startConnection(String ip, int port) {
    try {
      socket = new Socket(ip, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      userInput = new BufferedReader(new InputStreamReader(System.in));
    } catch (IOException e) {
      System.out.println("Error connecting to server. Exiting...");
      System.exit(1);
    }
  }

  public void sendMessage() {
    try {
      String message;
      while (!Objects.equals(message = userInput.readLine(), "exit")) {
        out.println(message);

        String response = in.readLine();
        System.out.println(response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stopConnection() {
    try {
      in.close();
      out.close();
      userInput.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.startConnection(Constants.SERVER_IP, Constants.SERVER_PORT);
    client.sendMessage();
    client.stopConnection();
  }
}
