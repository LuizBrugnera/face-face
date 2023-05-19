import java.io.BufferedReader;
import java.io.IOException;

public class MessageReceiver extends Thread {
  private final BufferedReader in;

  public MessageReceiver(BufferedReader in) {
    this.in = in;
  }

  @Override
  public void run() {
    try {
      String serverMessage;
      while ((serverMessage = in.readLine()) != null) {
        System.out.println(serverMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
