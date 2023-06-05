package common.logic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Personagem implements Serializable {
  private String nome;
  private String avatar;
  private CorPele corPele;
  private CorOlhos corOlhos;
  private CorCabelo corCabelo;
  private boolean sexo;
  private boolean temCabelo;
  private boolean usaOculos;
  private boolean usaChapeu;
}
