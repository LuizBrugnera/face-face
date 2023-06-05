package client.gui;

import common.logic.*;

import javax.swing.*;
import java.util.List;

public class PerguntaMenu extends JFrame {
  private final List<Personagem> personagens;
  private final JFrame parentFrame;

  public PerguntaMenu(List<Personagem> personagens, Tabuleiro parentFrame) {
    this.personagens = personagens;
    this.parentFrame = parentFrame;
  }

  public void iniciar(MenuCallback callback) {
    this.setTitle("Faça uma pergunta");
    this.setSize(350, 200);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();

    JButton cabeloButton = new JButton("Perguntar sobre cor do cabelo");
    JButton olhosButton = new JButton("Perguntar sobre cor dos olhos");
    JButton oculosButton = new JButton("Perguntar sobre uso de óculos");
    JButton escolherButton = new JButton("Fazer escolha");
    JButton sairButton = new JButton("Sair");

    cabeloButton.addActionListener(e -> {
      new EnumSelector<>(CorCabelo.class, "Escolha uma cor de cabelo", Trait.COR_CABELO, this)
              .iniciar(callback);
      this.dispose();
    });

    olhosButton.addActionListener(e -> {
      new EnumSelector<>(CorOlhos.class, "Escolha uma cor de olhos", Trait.COR_OLHOS, this)
              .iniciar(callback);
      this.dispose();
    });

    oculosButton.addActionListener(e -> {
      callback.onResult(new Pergunta(Trait.USA_OCULOS, 1));
      this.dispose();
    });

    escolherButton.addActionListener(e -> {
      new Escolher(personagens, parentFrame).iniciar(callback);
      this.dispose();
    });

    sairButton.addActionListener(e -> {
      callback.onResult(null);
      this.dispose();
    });

    panel.add(cabeloButton);
    panel.add(olhosButton);
    panel.add(oculosButton);
    panel.add(escolherButton);
    panel.add(sairButton);

    this.getContentPane().add(panel);
    this.setLocationRelativeTo(parentFrame);
    this.setVisible(true);
  }
}
