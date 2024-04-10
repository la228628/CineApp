package be.helha.applicine.views;


import javax.swing.*;
import java.awt.*;

public class WaitingWindowViewController {
    public Frame getWaitingWindow() {
        JFrame frame = new JFrame();
        frame.setSize(500, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Veuillez patienter pendant que la base de données se remplit...");
        frame.add(label);
        frame.setVisible(true);
        return frame;
    }
}