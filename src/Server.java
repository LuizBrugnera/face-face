import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {
  private ServerSocket serverSocket;
  private final Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

  public void start(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server started. Waiting for client...");
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (true) {
      try {
        ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), this);
        clientHandlers.add(clientHandler);
        clientHandler.start();
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

  public void broadcastMessage(String message, ClientHandler sender) {
    synchronized (clientHandlers) {
      for (ClientHandler clientHandler : clientHandlers) {
        if (clientHandler != sender) {
          clientHandler.sendMessage(message);
        }
      }
    }
  }

  public void removeClient(ClientHandler clientHandler) {
    clientHandlers.remove(clientHandler);
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.start(Constants.SERVER_PORT);
    server.stop();
  }
}
