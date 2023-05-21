import GameLogic.Personagem;
import GameLogic.Player;
import GameLogic.Trait;
import Utils.CharacterReader;

import java.util.List;
import java.util.Scanner;

public class SinglePlayer {

  public static void main(String[] args) {
    CharacterReader characterReader = new CharacterReader();
    List<Personagem> personagens = characterReader.lerPersonagens("/personagens.csv");

    Player player = new Player();
    player.tabuleiro = personagens;

    Player cpu = new Player();
    cpu.tabuleiro = personagens;
    cpu.personagem = cpu.tabuleiro.get((int) (Math.random() * cpu.tabuleiro.size()));

    while (true) {
      System.out.println("Tentativas restantes: " + (3 - player.tentativas));
      Scanner scanner = new Scanner(System.in);
      Menu.Pergunta menuPergunta = new Menu.Pergunta(scanner, personagens);
      GameLogic.Pergunta pergunta = menuPergunta.iniciar();

      // chute
      if (pergunta.trait == Trait.NOME) {
        Personagem target = player.tabuleiro.get(pergunta.value);

        if (target == cpu.personagem) {
          System.out.println("Você ganhou!");
        } else {
          System.out.println("Você perdeu!");
        }

        break;
      }

      if (player.tentativas == 3) {
        System.out.println("Você perdeu!");
        break;
      }

      player.fazerPergunta(pergunta, cpu.personagem);
    }

  }


}
