package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class p1Frame extends JFrame
{
    // Parent Panel
    JPanel parentPanel = new JPanel();      // Parent Panel


    p1Frame()
    {

        // Frames unbound
        this.setLocationRelativeTo(null);

        // Closure
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Resizable
        this.setResizable(false);

        // Panel to Hold : Buttons , Board , Prompt
        this.add(parentPanel);

        // Title
        this.setTitle("Reversi - white player");

        // BorderLayout with no Gaps between components
        parentPanel.setLayout(new BorderLayout());

    }  }