package tech.charliewilkins.BEST;

import java.awt.EventQueue;
import javax.swing.JFrame;

import tech.charliewilkins.BEST.World.World;

public class App extends JFrame {
    public App() {
        initUI();
    }

    private void initUI() {
        add(new World());

        setResizable(false);
        pack();

        setTitle("B.E.S.T.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame best = new App();
            best.setVisible(true);
        });
    }
}
