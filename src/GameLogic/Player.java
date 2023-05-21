package GameLogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
  public List<Personagem> tabuleiro;
  public Personagem personagem;
  public int tentativas;
  public int acertos;

  public void escolherPersonagem(Personagem personagem) {
    this.personagem = personagem;
  }

  public void fazerPergunta(Pergunta pergunta, Personagem target) {
    Map<Trait, TraitChecker> traitCheckers = new HashMap<>();
    traitCheckers.put(Trait.COR_OLHOS, (targetPersonagem) -> targetPersonagem.corOlhos.ordinal());
    traitCheckers.put(Trait.COR_CABELO, (targetPersonagem) -> targetPersonagem.corCabelo.ordinal());
    traitCheckers.put(Trait.USA_OCULOS, (targetPersonagem) -> targetPersonagem.usaOculos ? 1 : 0);

    int traitValue = traitCheckers.get(pergunta.trait).checkTrait(target);
    if (traitValue == pergunta.value) {
      System.out.println("Sim");
      this.acertos++;
    } else {
      System.out.println("Não");
      this.tentativas++;
    }
  }

  @FunctionalInterface
  interface TraitChecker {
    int checkTrait(Personagem target);
  }


}
