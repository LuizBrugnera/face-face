package common;

import common.logic.Pergunta;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class Protocol implements Serializable {

  private static final long serialVersionUID = 1L;

  public enum Type {
    CHARACTER_REQUEST, CHARACTER_RESPONSE,
    QUESTION_REQUEST, QUESTION_RESPONSE,
    GUESS_RESPONSE, CLOSE_CONNECTION, NONE
  }

  private Type requestType = Type.NONE;
  private Pergunta pergunta;
  @Setter
  private boolean resposta;
  @Setter
  private String message;
  private int characterId;

  public Protocol(String message) {
    this.message = message;
  }

  public Protocol(Type type) {
    this.requestType = type;
  }

  public Protocol(Type questionResponse, boolean resposta) {
    this.requestType = questionResponse;
    this.resposta = resposta;
  }

  public Protocol(Type characterResponse, int characterId) {
    this.requestType = characterResponse;
    this.characterId = characterId;
  }

  public Protocol(Type questionResponse, Pergunta pergunta) {
    this.requestType = questionResponse;
    this.pergunta = pergunta;
  }

  @Override
  public String toString() {
    return "Protocol{" + "requestType=" + requestType + ", pergunta=" + pergunta + ", resposta=" + resposta + ", message='" + message + '\'' + ", characterId=" + characterId + '}';
  }

}
