package GameLogic;

import java.io.Serializable;

public class Pergunta implements Serializable {
  public Trait trait;
  public int value;

  public Pergunta(Trait trait, int value) {
    this.trait = trait;
    this.value = value;
  }
}
