package common.logic;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Personagem implements Serializable {
  private String nome;
  private CorCabelo corCabelo;
  private CorOlhos corOlhos;
  private boolean usaOculos;
}
