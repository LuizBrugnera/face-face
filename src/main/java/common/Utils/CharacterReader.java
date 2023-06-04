package common.Utils;

import common.GameLogic.CorCabelo;
import common.GameLogic.CorOlhos;
import common.GameLogic.Personagem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CharacterReader {
  public List<Personagem> lerPersonagens(String arquivo) {
    List<Personagem> personagens = new ArrayList<>();
    String linha;

    InputStream inputStream = getClass().getResourceAsStream(arquivo);

    if (inputStream == null) {
      System.out.println("Erro: não foi possível abrir o arquivo " + arquivo);
      return personagens;
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      // Ler a linha do cabeçalho
      linha = br.readLine();

      while ((linha = br.readLine()) != null) {
        String[] campos = linha.split(",");
        Personagem personagem = new Personagem();
        personagem.nome = campos[0].trim();
        personagem.corCabelo = CorCabelo.valueOf(campos[1].trim());
        personagem.corOlhos = CorOlhos.valueOf(campos[2].trim());
        personagem.usaOculos = campos[3].trim().equalsIgnoreCase("sim");
        personagens.add(personagem);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return personagens;
  }
}
