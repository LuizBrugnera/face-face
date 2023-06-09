package client.gui;

import common.logic.Personagem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Tabuleiro extends JFrame {
  private String status = "Aguardando jogadores...";
  private JLabel statusLabel;

  private JLabel scoreLabel;

  public void iniciar(List<Personagem> personagens) {
    this.setTitle("Cara a Cara - Multiplayer");
    this.setSize(900, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    statusLabel = new JLabel(status);
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.PLAIN, 20));

    scoreLabel = new JLabel("Vitórias: 0 | Derrotas: 0");
    scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
    scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.PLAIN, 20));

    JPanel bottomPanel = new JPanel();
    listarPersonagens(personagens, bottomPanel);

    this.getContentPane().add(statusLabel, BorderLayout.PAGE_START);
    this.getContentPane().add(new JLabel("Tabuleiro"), BorderLayout.CENTER);
    this.getContentPane().add(bottomPanel);

    this.getContentPane().add(scoreLabel, BorderLayout.PAGE_END);

    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public void setStatus(String status) {
    this.status = status;
    statusLabel.setText(status);
  }

  public void setScore(String score) {
    scoreLabel.setText(score);
  }

  private static void listarPersonagens(List<Personagem> personagens, JPanel panel) {
    for (Personagem personagem : personagens) {
      JButton button = new JButton(personagem.getNome());

      Image image = new ImageIcon("src\\main\\resources\\images\\" + personagem.getAvatar()).getImage();
      image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
      button.setIcon(new ImageIcon(image));

      button.setSize(50, 50);
      button.setVerticalTextPosition(SwingConstants.BOTTOM);
      button.setHorizontalTextPosition(SwingConstants.CENTER);

      button.addActionListener(e -> {
        Color currentColor = button.getBackground();

        if (Color.RED.equals(currentColor)) {
          button.setBackground(Color.GREEN);
        } else if (Color.GREEN.equals(currentColor)) {
          button.setBackground(null);
        } else {
          button.setBackground(Color.RED);
        }
      });

      panel.add(button);
    }
  }

}
