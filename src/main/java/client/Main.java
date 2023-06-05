package client;

import client.gui.Escolher;
import client.gui.PerguntaMenu;
import client.gui.Popup;
import client.gui.Tabuleiro;
import common.Protocol;
import common.logic.Personagem;
import common.utils.CharacterReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static common.utils.Constants.*;

public class Main {

  private ObjectOutputStream out;
  private ObjectInputStream in;

  private List<Personagem> personagens;

  private Tabuleiro tabuleiro;

  private final Logger logger = LoggerFactory.getLogger(Main.class);

  public void start() {
    CharacterReader characterReader = new CharacterReader();
    personagens = characterReader.lerPersonagens(CHARACTER_FILE_PATH);
    tabuleiro = new Tabuleiro();

    try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
      setupConnection(socket);
      logger.info("Client started...");
      tabuleiro.iniciar(personagens);
      runClientLoop();

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void setupConnection(Socket socket) throws IOException {
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
  }

  private void runClientLoop() throws IOException, ClassNotFoundException {
    Protocol response;
    while ((response = (Protocol) in.readObject()) != null) {
      update(response);
    }
  }

  public void update(Protocol response) {
    logger.info("Receiving: " + response);

    if (response.getMessage() != null) {
      tabuleiro.setStatus(response.getMessage());
    }

    switch (response.getRequestType()) {
      case CHARACTER_REQUEST -> handleCharacterRequest();
      case QUESTION_RESPONSE -> handleQuestionResponse(response);
      case GUESS_RESPONSE -> handleGuessResponse(response);
      case QUESTION_REQUEST -> handleQuestionRequest();
      case CLOSE_CONNECTION -> handleCloseConnection();
    }
  }

  private void handleGuessResponse(Protocol response) {
    String popupText = response.getMessage();
    new Popup().iniciar(popupText, tabuleiro);
  }

  private void handleQuestionResponse(Protocol response) {
    String popupText = "Resposta:" + (response.isResposta() ? " Sim" : " Não");
    new Popup().iniciar(popupText, tabuleiro);
  }

  private void handleCloseConnection() {
    logger.info("Closing connection...");
    System.exit(0);
  }

  private void handleCharacterRequest() {
    Escolher escolher = new Escolher(personagens, tabuleiro);

    escolher.iniciar(pergunta -> {
      Protocol protocol = new Protocol(Protocol.Type.CHARACTER_RESPONSE, pergunta.value());
      send(protocol);
    });
  }

  private void send(Protocol protocol) {
    logger.info("Sending: " + protocol);

    try {
      out.writeObject(protocol);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleQuestionRequest() {
    PerguntaMenu menuPergunta = new PerguntaMenu(personagens, tabuleiro);

    menuPergunta.iniciar(pergunta -> {
      Protocol protocol = new Protocol(Protocol.Type.QUESTION_RESPONSE, pergunta);
      send(protocol);
    });
  }


  public static void main(String[] args) {
    Main client = new Main();
    client.start();
  }
}
