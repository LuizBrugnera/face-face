package client.gui;

import common.logic.Pergunta;
import common.logic.Personagem;
import common.logic.Trait;

import javax.swing.*;
import java.util.List;

public class Escolher extends JFrame {
  private final List<Personagem> personagens;

  private final JFrame parentFrame;

  public Escolher(List<Personagem> personagens, JFrame parentFrame) {
    this.personagens = personagens;
    this.parentFrame = parentFrame;
  }

  public void iniciar(MenuCallback callback) {
    this.setTitle("Escolha um personagem");
    this.setSize(350, 200);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();

    DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

    for (Personagem personagem : personagens) {
      comboBoxModel.addElement(personagem.getNome());
    }

    JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);

    JButton button = new JButton("Confirmar");

    button.addActionListener(e -> {
      int selectedIndex = comboBox.getSelectedIndex();
      if (selectedIndex != -1) {
        callback.onResult(new Pergunta(Trait.NOME, selectedIndex));
        this.dispose();
      } else {
        JOptionPane.showMessageDialog(this, "Por favor, selecione um personagem.");
      }
    });

    panel.add(comboBox);
    panel.add(button);

    this.getContentPane().add(panel);
    this.setLocationRelativeTo(parentFrame);
    this.setVisible(true);
  }
}
