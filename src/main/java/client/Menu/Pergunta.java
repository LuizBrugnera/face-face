package client.Menu;

import common.GameLogic.Personagem;
import common.GameLogic.Trait;

import java.util.List;
import java.util.Scanner;

public class Pergunta {
  private final Scanner scanner;
  private final List<Personagem> personagens;

  public Pergunta(Scanner scanner, List<Personagem> personagens) {
    this.scanner = scanner;
    this.personagens = personagens;
  }

  public common.GameLogic.Pergunta iniciar() {
    boolean sair = false;

    while (!sair) {
      System.out.println("\nMenu:");
      System.out.println("1 - Perguntar sobre cor do cabelo");
      System.out.println("2 - Perguntar sobre cor dos olhos");
      System.out.println("3 - Perguntar sobre uso de óculos");
      System.out.println("4 - Fazer escolha");
      System.out.println("5 - Sair");

      int escolha = scanner.nextInt();

      switch (escolha) {
        case 1:
          return new Cabelo(scanner).iniciar();
        case 2:
          return new Olhos(scanner).iniciar();
        case 3:
          return new common.GameLogic.Pergunta(Trait.USA_OCULOS, 1);
        case 4:
          return new Escolher(scanner, personagens).iniciar();
        case 5:
          sair = true;
          break;
        default:
          System.out.println("Opção inválida. Por favor, escolha uma opção entre 1 e 4.");
          break;
      }
    }

    return null;
  }
}
