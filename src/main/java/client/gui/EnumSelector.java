package client.gui;

import common.logic.Pergunta;
import common.logic.Trait;

import javax.swing.*;

public class EnumSelector<T extends Enum<T>> extends JFrame {

  private final Class<T> enumClass;
  private final String windowTitle;
  private final Trait trait;

  private final JFrame parentFrame;

  public EnumSelector(Class<T> enumClass, String windowTitle, Trait trait, JFrame parentFrame) {
    this.enumClass = enumClass;
    this.windowTitle = windowTitle;
    this.trait = trait;
    this.parentFrame = parentFrame;
  }

  public void iniciar(MenuCallback callback) {
    this.setTitle(windowTitle);
    this.setSize(350, 200);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    JLabel label = new JLabel();

    T[] enumValues = enumClass.getEnumConstants();
    JComboBox<T> comboBox = new JComboBox<>(enumValues);

    JButton button = new JButton("Confirmar");

    button.addActionListener(e -> {
      @SuppressWarnings("unchecked")
      T selectedValue = (T) comboBox.getSelectedItem();

      label.setText("Você escolheu: " + selectedValue);
      callback.onResult(new Pergunta(trait, comboBox.getSelectedIndex()));
      this.dispose();
    });

    panel.add(comboBox);
    panel.add(button);
    panel.add(label);

    this.getContentPane().add(panel);
    this.setLocationRelativeTo(parentFrame);
    this.setVisible(true);
  }
}
