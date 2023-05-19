import java.io.IOException;
import java.net.ServerSocket;

public class Server {
  private ServerSocket serverSocket;

  public void start(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server started. Waiting for client...");
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (true) {
      try {
        new ClientHandler(serverSocket.accept()).start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void stop() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    Server server = new Server();
    server.start(Constants.SERVER_PORT);
    server.stop();
  }
}
