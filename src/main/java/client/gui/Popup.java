package client.gui;

import javax.swing.*;
import java.awt.*;

public class Popup extends JFrame {
  public void iniciar(String response, JFrame parentFrame) {
    this.setTitle("Resposta");
    this.setSize(300, 150);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());

    JLabel responseLabel = new JLabel(response);
    responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(responseLabel, BorderLayout.CENTER);

    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> this.dispose());

    panel.add(okButton, BorderLayout.PAGE_END);

    this.getContentPane().add(panel);
    this.setLocationRelativeTo(parentFrame);
    this.setVisible(true);
  }
}
