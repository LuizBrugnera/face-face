package server;

import common.logic.Pergunta;
import common.logic.Personagem;
import common.logic.Player;
import common.logic.Trait;
import common.utils.CharacterReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static common.utils.Constants.CHARACTER_FILE_PATH;
import static common.utils.Constants.SERVER_PORT;

public class Main {

  private ClientHandler clientHandler1;
  private ClientHandler clientHandler2;
  private Player player1;
  private Player player2;

  private boolean endGame = false;

  private final Logger logger = LoggerFactory.getLogger(Main.class);

  public void start(int port) {
    CharacterReader characterReader = new CharacterReader();
    List<Personagem> personagens = characterReader.lerPersonagens(CHARACTER_FILE_PATH);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      initializePlayers(serverSocket);

      assignCharacterToPlayer(personagens, clientHandler1, true);
      assignCharacterToPlayer(personagens, clientHandler2, false);

      startGameLoop(personagens);

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      clientHandler1.close();
      clientHandler2.close();
    }
  }

  private void startGameLoop(List<Personagem> personagens) throws IOException, ClassNotFoundException {
    do {
      handleQuestionLoop(personagens, clientHandler1, player1, player2, "1");
      if (endGame) {
        break;
      }
      handleQuestionLoop(personagens, clientHandler2, player2, player1, "2");
    } while (!endGame);
  }

  private void handleQuestionLoop(List<Personagem> personagens, ClientHandler clientHandler, Player askingPlayer, Player answeringPlayer, String playerId) throws IOException, ClassNotFoundException {
    clientHandler.sendMessage("Jogador " + playerId + " está fazendo uma pergunta...");
    Pergunta pergunta = clientHandler.sendQuestionRequest();

    if (pergunta == null) {
      broadcastMessage("Jogador " + playerId + " passou a vez.");
      return;
    }

    boolean isGuess = pergunta.trait() == Trait.NOME;
    if (isGuess) {
      handlePlayerGuess(answeringPlayer, personagens, pergunta, playerId);
      return;
    }

    boolean resposta = askingPlayer.fazerPergunta(pergunta, answeringPlayer.getPersonagem());
    clientHandler.sendQuestionResponse(resposta, false);
  }

  private void handlePlayerGuess(Player answeringPlayer, List<Personagem> personagens, Pergunta pergunta, String playerId) throws IOException {
    Personagem guess = personagens.get(pergunta.value());

    boolean isCorrect = answeringPlayer.getPersonagem().equals(guess);

    broadcastMessage("Jogador " + playerId + " chutou " + guess.getNome() + " e " + (isCorrect ? "ganhou!" : "perdeu!"));
    broadcastMessage("Encerrando jogo...");

    clientHandler1.sendQuestionResponse(isCorrect, true);
    endGame = true;
  }

  private void broadcastMessage(String message) throws IOException {
    clientHandler1.sendMessage(message);
    clientHandler2.sendMessage(message);
  }

  private void initializePlayers(ServerSocket serverSocket) throws IOException {
    logger.info("Server started, waiting for player 1...");
    clientHandler1 = new ClientHandler(serverSocket.accept());

    logger.info("Player 1 connected, waiting for player 2...");
    clientHandler1.sendMessage("Esperando o jogador 2...");

    clientHandler2 = new ClientHandler(serverSocket.accept());
    logger.info("Player 2 connected, starting game...");
  }

  private void assignCharacterToPlayer(List<Personagem> personagens, ClientHandler clientHandler, boolean isFirstPlayer) throws IOException, ClassNotFoundException {
    int characterId;
    do {
      characterId = clientHandler.sendCharacterRequest();
    } while (characterId < 0 || characterId >= personagens.size());

    Player player = new Player();
    player.setPersonagem(personagens.get(characterId));

    if (isFirstPlayer) {
      player1 = player;
    } else {
      player2 = player;
    }

    String playerId = isFirstPlayer ? "1" : "2";
    logger.info("Player " + playerId + " chose " + player.getPersonagem().getNome());
  }

  public static void main(String[] args) {
    Main server = new Main();
    server.start(SERVER_PORT);
  }
}