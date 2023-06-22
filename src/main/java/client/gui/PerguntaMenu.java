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
    this.setSize(300, 400);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();

    JButton sexoButton = new JButton("Perguntar sobre sexo");
    JButton temCabeloButton = new JButton("Perguntar se tem cabelo");
    JButton corPeleButton = new JButton("Perguntar sobre cor da pele");
    JButton cabeloButton = new JButton("Perguntar sobre cor do cabelo");
    JButton olhosButton = new JButton("Perguntar sobre cor dos olhos");
    JButton oculosButton = new JButton("Perguntar sobre uso de óculos");
    JButton chapeuButton = new JButton("Perguntar sobre uso de chapéu");
    JButton escolherButton = new JButton("Fazer escolha");
    JButton sairButton = new JButton("Sair do jogo");

    cabeloButton.addActionListener(e -> {
      new EnumSelector<>(CorCabelo.class, "Escolha uma cor de cabelo", Trait.COR_CABELO, this).iniciar(callback);
      this.dispose();
    });

    olhosButton.addActionListener(e -> {
      new EnumSelector<>(CorOlhos.class, "Escolha uma cor de olhos", Trait.COR_OLHOS, this).iniciar(callback);
      this.dispose();
    });

    corPeleButton.addActionListener(e -> {
      new EnumSelector<>(CorPele.class, "Escolha uma cor de pele", Trait.COR_PELE, this).iniciar(callback);
      this.dispose();
    });

    sexoButton.addActionListener(e -> {
      callback.onResult(new Pergunta(Trait.SEXO, 1));
      this.dispose();
    });

    chapeuButton.addActionListener(e -> {
      callback.onResult(new Pergunta(Trait.USA_CHAPEU, 1));
      this.dispose();
    });

    temCabeloButton.addActionListener(e -> {
      callback.onResult(new Pergunta(Trait.TEM_CABELO, 1));
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

    panel.add(sexoButton);
    panel.add(temCabeloButton);
    panel.add(corPeleButton);
    panel.add(chapeuButton);
    panel.add(cabeloButton);
    panel.add(olhosButton);
    panel.add(oculosButton);
    panel.add(escolherButton);
    panel.add(sairButton);

    // when disposing this frame, return the callback with null
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        callback.onResult(null);
      }
    });

    this.getContentPane().add(panel);
    this.setLocationRelativeTo(parentFrame);
    this.setVisible(true);
  }

}
