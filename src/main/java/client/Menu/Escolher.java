package client.Menu;

import common.GameLogic.Personagem;

import java.util.List;
import java.util.Scanner;

public class Escolher {
  private final Scanner scanner;

  private final List<Personagem> personagens;

  public Escolher(Scanner scanner, List<Personagem> personagens) {
    this.scanner = scanner;
    this.personagens = personagens;
  }

  public common.GameLogic.Pergunta iniciar() {
    System.out.println("\nEscolha um personagem:");
    for (int i = 0; i < personagens.size(); i++) {
      System.out.printf("%d - %s\n", i, personagens.get(i).nome);
    }
    int escolha = scanner.nextInt();
    return new common.GameLogic.Pergunta(common.GameLogic.Trait.NOME, escolha);
  }
}