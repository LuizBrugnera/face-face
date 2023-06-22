package server;

import common.Protocol;
import common.logic.Pergunta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
  private final Socket clientSocket;
  private ObjectInputStream in;
  private ObjectOutputStream out;

  public void sendMessage(String message) throws IOException {
    Protocol protocol = new Protocol(message);
    out.writeObject(protocol);
    out.flush();
  }

  public int sendCharacterRequest() throws IOException, ClassNotFoundException {
    Protocol protocol = new Protocol(Protocol.Type.CHARACTER_REQUEST);
    out.writeObject(protocol);
    out.flush();

    Protocol response = (Protocol) in.readObject();
    return response.getCharacterId();
  }

  public Pergunta sendQuestionRequest() throws IOException, ClassNotFoundException {
    Protocol protocol = new Protocol(Protocol.Type.QUESTION_REQUEST);
    out.writeObject(protocol);
    out.flush();

    Protocol response = (Protocol) in.readObject();
    return response.getPergunta();
  }

  public void sendQuestionResponse(boolean resposta, boolean isGuess) {
    Protocol.Type type = isGuess ? Protocol.Type.GUESS_RESPONSE : Protocol.Type.QUESTION_RESPONSE;
    Protocol protocol = new Protocol(type, resposta);

    try {
      out.writeObject(protocol);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendGuessResponse(String message, boolean isCorrect) {
    Protocol protocol = new Protocol(Protocol.Type.GUESS_RESPONSE);

    protocol.setMessage(message);
    protocol.setResposta(isCorrect);

    try {
      out.writeObject(protocol);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      sendCloseConnection();
      in.close();
      out.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendCloseConnection() throws IOException {
    Protocol protocol = new Protocol(Protocol.Type.CLOSE_CONNECTION);
    out.writeObject(protocol);
    out.flush();
  }

  public ClientHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;

    Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    logger.info("New client connected: " + clientSocket);

    try {
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
