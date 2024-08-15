package main;

import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        // Close window properly when user clicks the close ("x") button
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        // Window will display at the center of the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();

    }
}