package Menu;

import GameLogic.CorCabelo;
import GameLogic.Pergunta;
import GameLogic.Trait;

import java.util.Scanner;

public class Cabelo {
  private final Scanner scanner;

  public Cabelo(Scanner scanner) {
    this.scanner = scanner;
  }

  public Pergunta iniciar() {

    while (true) {
      System.out.println("\nEscolha uma cor de cabelo:");

      CorCabelo[] cores = CorCabelo.values();
      for (int i = 0; i < cores.length; i++) {
        System.out.printf("%d - %s\n", i + 1, cores[i]);
      }

      int escolha = scanner.nextInt();
      if (escolha < 1 || escolha > cores.length) {
        System.out.println("Opção inválida. Por favor, escolha uma opção entre 1 e " + cores.length + ".");
        continue;
      }

      CorCabelo corEscolhida = cores[escolha - 1];
      System.out.println("Você escolheu: " + corEscolhida);

      return new Pergunta(Trait.COR_CABELO, escolha - 1);
    }
  }

}
