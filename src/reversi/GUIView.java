package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIView implements IView {
    IModel model;
    IController controller;

    JLabel pP1 = new JLabel();
    JLabel pP2 = new JLabel();        // Prompts
    JPanel board1 = new JPanel();
    JPanel board2 = new JPanel();     // Boards
    JPanel buttons1 = new JPanel();
    JPanel buttons2 = new JPanel();   // Buttons
    p1Frame p1f = new p1Frame();
    p2Frame p2f = new p2Frame();      // Frames
    int bW = 500;
    int bH = 500;
    int xDim;
    int yDim;

    // Constructor
    public GUIView() {

    }

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;
        this.xDim = model.getBoardWidth();
        this.yDim = model.getBoardHeight();


        // P1 + P2 prompts
        pP1.setFont(new Font("Arial", Font.BOLD, 16));
        pP1.setText("P1 prompt here");
        p1f.parentPanel.add(pP1, BorderLayout.NORTH);
        pP2.setFont(new Font("Arial", Font.BOLD, 16));
        pP2.setText("P2 prompt here");
        p2f.parentPanel.add(pP2, BorderLayout.NORTH);

        // Board Meta Data
        board1.setPreferredSize(new Dimension(bW, bH));
        board1.setBackground(Color.BLACK);
        board2.setPreferredSize(new Dimension(bW, bH));
        board2.setBackground(Color.BLACK);

        // Dimensions for gridLayout of Game Panels
        board1.setLayout(new GridLayout(xDim, yDim));
        board2.setLayout(new GridLayout(xDim, yDim));

        for (int i = 0; i < xDim; i++) {       // X loop
            for (int j = 0; j < yDim; j++) {   // Y Loop
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    // creates a white reversi disk on square (i,j)
                    board1.add(new Square2(i, j, 1, p1f, model, controller, 1));
                    board2.add(new Square2(i, j, 1, p2f, model, controller, 2));

                } else if ((i == 3 && j == 4) || (i == 4 && j == 3)) {
                    board1.add(new Square2(i, j, 2, p1f, model, controller, 1));
                    board2.add(new Square2(i, j, 2, p2f, model, controller, 2));

                } else {
                    board1.add(new Square2(i, j, 0, p1f, model, controller, 1));
                    board2.add(new Square2(i, j, 0, p2f, model, controller, 2));

                }
            }
        }

        // Boards Added to Parent Frame
        p1f.parentPanel.add(board1, BorderLayout.CENTER);
        p2f.parentPanel.add(board2, BorderLayout.CENTER);

        // Button frame Layout Manager
        buttons1.setLayout(new BorderLayout());
        buttons2.setLayout(new BorderLayout());

        // P1 + P2 AI buttons
        JButton AiBw = new JButton("Greedy AI (play white)");
        AiBw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(1);
            }
        });
        buttons1.add(AiBw, BorderLayout.NORTH);

        JButton AiBb = new JButton("Greedy AI (play black)");
        AiBb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(2);
            }
        });
        buttons2.add(AiBb, BorderLayout.NORTH);

        // Restart
        JButton restartW = new JButton("Restart");
        restartW.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.startup();
                refreshView();
            }
        });
        buttons1.add(restartW, BorderLayout.SOUTH);

        // Restart button
        JButton restartB = new JButton("Restart");
        restartB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.startup();
                refreshView();
            }
        });
        buttons2.add(restartB, BorderLayout.SOUTH);

        // Button Frame
        p1f.parentPanel.add(buttons1, BorderLayout.SOUTH);
        p2f.parentPanel.add(buttons2, BorderLayout.SOUTH);

        // Pack frame together
        p2f.pack();
        p1f.pack();

        // Visibility
        p1f.setVisible(true);
        p2f.setVisible(true);

    }

    public JPanel buildBoardP1() {
        // Create a new board which reflects the one stored in the model
        // Using the player parameter to indicate what color a square should be

        JPanel newPanel = new JPanel();
        newPanel.setPreferredSize(new Dimension(bW, bH));
        newPanel.setBackground(Color.WHITE);
        newPanel.setLayout(new GridLayout(this.xDim, this.yDim));

        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                switch (model.getBoardContents(i, j)) {
                    case 1 ->        // White
                            newPanel.add(new Square2(i, j, 1, p1f, model, controller, 1));
                    case 2 ->        // Black
                            newPanel.add(new Square2(i, j, 2, p1f, model, controller, 1));
                    default ->       // Empty
                            newPanel.add(new Square2(i, j, 0, p1f, model, controller, 1));
                }

            }
        }


        return newPanel;
    }

    public JPanel buildBoardP2() {
        // Refresh the appearance based on data in the model
        JPanel newPanel = new JPanel();
        newPanel.setPreferredSize(new Dimension(bW, bH));
        newPanel.setBackground(Color.BLACK);
        newPanel.setLayout(new GridLayout(this.xDim, this.yDim));

        for (int i = xDim - 1; i >= 0; i--) {
            for (int j = yDim - 1; j >= 0; j--) {
                switch (model.getBoardContents(i, j)) {
                    case 1 ->        // White
                            newPanel.add(new Square2(i, j, 1, p1f, model, controller, 2));
                    case 2 ->     // Black
                            newPanel.add(new Square2(i, j, 2, p1f, model, controller, 2));
                    default -> // Empty
                            newPanel.add(new Square2(i, j, 0, p1f, model, controller, 2));

                }
            }
        }


        return newPanel;
    }


    @Override
    public void refreshView() {
        JPanel newB1 = new JPanel();
        JPanel newB2 = new JPanel();

        // Create a new board and replace the one in the JFrame with the one that reflects the model

        newB1 = buildBoardP1();
        newB2 = buildBoardP2();


        BorderLayout layout1 = (BorderLayout) p1f.parentPanel.getLayout();
        p1f.parentPanel.remove(layout1.getLayoutComponent(BorderLayout.CENTER));

        BorderLayout layout2 = (BorderLayout) p2f.parentPanel.getLayout();
        p2f.parentPanel.remove(layout2.getLayoutComponent(BorderLayout.CENTER));

        p1f.parentPanel.add(newB1, BorderLayout.CENTER);
        p1f.validate();
        p1f.repaint();

        p2f.parentPanel.add(newB2, BorderLayout.CENTER);
        p2f.validate();
        p2f.repaint();

    }

    @Override
    public void feedbackToUser(int player, String message) {
        if (player == 1){ pP1.setText(message); }
        else if (player == 2){ pP2.setText(message); }

    }

}