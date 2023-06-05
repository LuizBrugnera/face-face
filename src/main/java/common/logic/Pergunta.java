package common.logic;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Pergunta implements Serializable {
  private final Trait trait;
  private final int value;

  public Pergunta(Trait trait, int value) {
    this.trait = trait;
    this.value = value;
  }
}
