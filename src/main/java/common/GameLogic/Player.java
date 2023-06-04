package common.GameLogic;

import java.util.HashMap;
import java.util.Map;

public class Player {
  public Personagem personagem;
  public int tentativas;
  public int acertos;

  public void setPersonagem(Personagem personagem) {
    this.personagem = personagem;
  }

  public Personagem getPersonagem() {
    return this.personagem;
  }

  public boolean fazerPergunta(Pergunta pergunta, Personagem target) {
    Map<Trait, TraitChecker> traitCheckers = new HashMap<>();
    traitCheckers.put(Trait.COR_OLHOS, (targetPersonagem) -> targetPersonagem.corOlhos.ordinal());
    traitCheckers.put(Trait.COR_CABELO, (targetPersonagem) -> targetPersonagem.corCabelo.ordinal());
    traitCheckers.put(Trait.USA_OCULOS, (targetPersonagem) -> targetPersonagem.usaOculos ? 1 : 0);

    int traitValue = traitCheckers.get(pergunta.trait).checkTrait(target);
    if (traitValue == pergunta.value) {
      this.acertos++;
      return true;
    } else {
      this.tentativas++;
      return false;
    }
  }

  @FunctionalInterface
  interface TraitChecker {
    int checkTrait(Personagem target);
  }


}
