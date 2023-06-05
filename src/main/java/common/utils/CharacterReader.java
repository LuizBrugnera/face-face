package common.utils;

import common.logic.*;

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
      br.readLine();

      while ((linha = br.readLine()) != null) {
        String[] campos = linha.split(",");
        Personagem personagem = new Personagem();
        personagem.setNome(campos[0].trim());
        personagem.setAvatar(campos[1].trim());
        personagem.setCorPele(CorPele.valueOf(campos[2].trim()));
        personagem.setCorCabelo(CorCabelo.valueOf(campos[3].trim()));
        personagem.setCorOlhos(CorOlhos.valueOf(campos[4].trim()));
        personagem.setSexo(campos[5].trim().equalsIgnoreCase("MASCULINO"));
        personagem.setTemCabelo(campos[6].trim().equalsIgnoreCase("SIM"));
        personagem.setUsaOculos(campos[7].trim().equalsIgnoreCase("SIM"));
        personagem.setUsaChapeu(campos[8].trim().equalsIgnoreCase("SIM"));
        personagens.add(personagem);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return personagens;
  }
}
