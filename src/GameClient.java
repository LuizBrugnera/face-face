import GameLogic.Personagem;
import Utils.CharacterReader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static Utils.Constants.*;

public class GameClient {

  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Scanner scanner;

  private List<Personagem> personagens;

  public void start() {
    CharacterReader characterReader = new CharacterReader();
    personagens = characterReader.lerPersonagens(CHARACTER_FILE_PATH);

    try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
      setupConnection(socket);
      System.out.println("Client started...");
      runClientLoop();

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void setupConnection(Socket socket) throws IOException {
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
    scanner = new Scanner(System.in);
  }

  private void runClientLoop() throws IOException, ClassNotFoundException {
    Protocol response;
    while ((response = (Protocol) in.readObject()) != null) {
      update(response);
    }
  }

  public void update(Protocol response) throws IOException {
    if (response.getMessage() != null) {
      System.out.println(response.getMessage());
    }

    switch (response.getRequestType()) {
      case CHARACTER_REQUEST -> handleCharacterRequest();
      case QUESTION_REQUEST -> handleQuestionRequest();
      case CLOSE_CONNECTION -> {
        System.out.println("Closing connection...");
        System.exit(0);
      }
    }
  }

  private void handleCharacterRequest() throws IOException {
    System.out.println("Enter a number:");
    int characterId = scanner.nextInt();

    Protocol protocol = new Protocol(Protocol.Type.CHARACTER_RESPONSE, characterId);

    out.writeObject(protocol);
    out.flush();
  }

  private void handleQuestionRequest() throws IOException {
    Menu.Pergunta menuPergunta = new Menu.Pergunta(scanner, personagens);
    GameLogic.Pergunta pergunta = menuPergunta.iniciar();

    Protocol protocol = new Protocol(Protocol.Type.QUESTION_RESPONSE, pergunta);

    out.writeObject(protocol);
  }

  public static void main(String[] args) {
    GameClient client = new GameClient();
    client.start();
  }
}
