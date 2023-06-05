package client;

import client.gui.Escolher;
import client.gui.PerguntaMenu;
import client.gui.Popup;
import client.gui.Tabuleiro;
import common.Protocol;
import common.logic.Personagem;
import common.utils.CharacterReader;
import org.apache.commons.cli.*;
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

  public void start(String serverHost, int port) {
    CharacterReader characterReader = new CharacterReader();
    personagens = characterReader.lerPersonagens(CHARACTER_FILE_PATH);
    tabuleiro = new Tabuleiro();

    try (Socket socket = new Socket(serverHost, port)) {
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
      case CHARACTER_REQUEST:
        handleCharacterRequest();
        break;
      case QUESTION_REQUEST:
        handleQuestionRequest();
        break;
      case GUESS_RESPONSE:
        handleGuessResponse(response);
        break;
      case QUESTION_RESPONSE:
        handleQuestionResponse(response);
        break;
      case CLOSE_CONNECTION:
        handleCloseConnection();
        break;
      default:
        break;
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
      Protocol protocol = new Protocol(Protocol.Type.CHARACTER_RESPONSE, pergunta.getValue());
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
    Options options = new Options();
    Option port = new Option("p", "port", true, "server port");
    Option host = new Option("h", "host", true, "server host");
    port.setRequired(false);
    options.addOption(host);
    options.addOption(port);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("face-face", options);
      System.exit(1);
    }

    int serverPort = Integer.parseInt(cmd.getOptionValue("port", DEFAULT_PORT));
    String serverHost = cmd.getOptionValue("host", DEFAULT_HOST);

    Main client = new Main();
    client.start(serverHost, serverPort);
  }
}
