package server;

import common.GameLogic.Pergunta;
import common.GameLogic.Personagem;
import common.GameLogic.Player;
import common.GameLogic.Trait;
import common.Utils.CharacterReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static common.Utils.Constants.CHARACTER_FILE_PATH;
import static common.Utils.Constants.SERVER_PORT;

public class Main {

  private ClientHandler clientHandler1;
  private ClientHandler clientHandler2;
  private Player player1;
  private Player player2;

  private boolean endGame = false;

  public void start(int port) {
    CharacterReader characterReader = new CharacterReader();
    List<Personagem> personagens = characterReader.lerPersonagens(CHARACTER_FILE_PATH);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      initializePlayers(serverSocket);

      presentCharacters(personagens, clientHandler1);
      assignCharacterToPlayer(personagens, clientHandler1, true);

      presentCharacters(personagens, clientHandler2);
      assignCharacterToPlayer(personagens, clientHandler2, false);

      startGameLoop(personagens);

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      clientHandler1.close();
      clientHandler2.close();
    }
  }

  private void startGameLoop(List<Personagem> personagems) throws IOException, ClassNotFoundException {
    do {
      handleQuestionLoop(personagems, clientHandler1, player1, player2, "1");
      if (endGame) {
        break;
      }
      handleQuestionLoop(personagems, clientHandler2, player2, player1, "2");
    } while (!endGame);
  }

  private void handleQuestionLoop(List<Personagem> personagems, ClientHandler clientHandler, Player askingPlayer, Player answeringPlayer, String playerId) throws IOException, ClassNotFoundException {
    clientHandler.sendMessage("Player " + playerId + " is making a question...");
    Pergunta pergunta = clientHandler.sendQuestionRequest();

    if (pergunta == null) {
      broadcastMessage("Player " + playerId + " passed his turn");
      return;
    }

    boolean isGuess = pergunta.trait == Trait.NOME;
    if (isGuess) {
      handlePlayerGuess(answeringPlayer, personagems, pergunta, playerId);
      return;
    }

    boolean resposta = askingPlayer.fazerPergunta(pergunta, answeringPlayer.getPersonagem());
    clientHandler.sendMessage("Resposta: " + (resposta ? "Sim" : "Não"));
  }

  private void handlePlayerGuess(Player answeringPlayer, List<Personagem> personagems, Pergunta pergunta, String playerId) throws IOException {
    Personagem guess = personagems.get(pergunta.value);

    boolean isCorrect = answeringPlayer.getPersonagem().equals(guess);

    broadcastMessage("Player " + playerId + " guessed " + guess.nome + " and " + (isCorrect ? "won!" : "lost!"));
    broadcastMessage("Ending game...");
    endGame = true;
  }

  private void broadcastMessage(String message) throws IOException {
    clientHandler1.sendMessage(message);
    clientHandler2.sendMessage(message);
  }

  private void initializePlayers(ServerSocket serverSocket) throws IOException {
    System.out.println("Server started, waiting for player 1...");
    clientHandler1 = new ClientHandler(serverSocket.accept());

    System.out.println("Player 1 connected, waiting for player 2...");
    clientHandler1.sendMessage("Waiting for player 2...");

    clientHandler2 = new ClientHandler(serverSocket.accept());
    System.out.println("Player 2 connected, starting game...");
  }

  private void presentCharacters(List<Personagem> personagens, ClientHandler clientHandler) throws IOException {
    clientHandler.sendMessage("Choose a character:");
    for (int i = 0; i < personagens.size(); i++) {
      clientHandler.sendMessage(i + " - " + personagens.get(i).nome);
    }
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
    System.out.println("Player " + playerId + " chose " + player.getPersonagem().nome);
  }

  public static void main(String[] args) {
    Main server = new Main();
    server.start(SERVER_PORT);
  }
}