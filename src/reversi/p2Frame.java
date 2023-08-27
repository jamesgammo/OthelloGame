package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class p2Frame extends JFrame
{
    JPanel parentPanel = new JPanel();

    p2Frame()
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
        this.setTitle("Reversi - black player");

        // BorderLayout with no Gaps between components
        parentPanel.setLayout(new BorderLayout());

    }  }