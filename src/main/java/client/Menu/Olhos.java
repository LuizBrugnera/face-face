package client.Menu;

import common.GameLogic.CorOlhos;
import common.GameLogic.Pergunta;
import common.GameLogic.Trait;

import java.util.Scanner;

public class Olhos {
  private final Scanner scanner;

  public Olhos(Scanner scanner) {
    this.scanner = scanner;
  }

  public Pergunta iniciar() {

    while (true) {
      System.out.println("\nEscolha uma cor de Olhos:");

      CorOlhos[] cores = CorOlhos.values();
      for (int i = 0; i < cores.length; i++) {
        System.out.printf("%d - %s\n", i + 1, cores[i]);
      }

      int escolha = scanner.nextInt();
      if (escolha < 1 || escolha > cores.length) {
        System.out.println("Opção inválida. Por favor, escolha uma opção entre 1 e " + cores.length + ".");
        continue;
      }

      CorOlhos corEscolhida = cores[escolha - 1];
      System.out.println("Você escolheu: " + corEscolhida);

      return new Pergunta(Trait.COR_OLHOS, escolha - 1);
    }
  }

}
