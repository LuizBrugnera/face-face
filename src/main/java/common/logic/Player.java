package common.logic;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Player {
  private Personagem personagem;

  public boolean fazerPergunta(Pergunta pergunta, Personagem target) {
    Map<Trait, TraitChecker> traitCheckers = new HashMap<>();
    traitCheckers.put(Trait.COR_OLHOS, (targetPersonagem) -> targetPersonagem.getCorOlhos().ordinal());
    traitCheckers.put(Trait.COR_CABELO, (targetPersonagem) -> targetPersonagem.getCorCabelo().ordinal());
    traitCheckers.put(Trait.USA_OCULOS, (targetPersonagem) -> targetPersonagem.isUsaOculos() ? 1 : 0);

    int traitValue = traitCheckers.get(pergunta.trait()).checkTrait(target);

    return traitValue == pergunta.value();
  }

  @FunctionalInterface
  interface TraitChecker {
    int checkTrait(Personagem target);
  }

}
