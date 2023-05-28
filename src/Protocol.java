import GameLogic.Pergunta;

import java.io.Serial;
import java.io.Serializable;

public class Protocol implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public enum Type {
    CHARACTER_REQUEST, CHARACTER_RESPONSE,

    QUESTION_REQUEST, QUESTION_RESPONSE,

    CLOSE_CONNECTION, NONE
  }

  private Type requestType = Type.NONE;
  private Pergunta pergunta;
  private String message;
  private int characterId;

  public Protocol(String message) {
    this.message = message;
  }

  public Protocol(Type type) {
    this.requestType = type;
  }

  public String getMessage() {
    return message;
  }

  public Protocol(Type characterResponse, int characterId) {
    this.requestType = characterResponse;
    this.characterId = characterId;
  }

  public Protocol(Type questionResponse, Pergunta pergunta) {
    this.requestType = questionResponse;
    this.pergunta = pergunta;
  }

  public Type getRequestType() {
    return requestType;
  }

  public int getCharacterId() {
    return characterId;
  }

  public Pergunta getPergunta() {
    return pergunta;
  }
}
