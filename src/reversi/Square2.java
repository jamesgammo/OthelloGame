package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Square2 extends JLabel implements MouseListener {
    IModel model;
    IController controller;
    JFrame current;
    int xPos;
    int yPos;
    int player;
    int board;

    public Square2(int i, int j, int player, JFrame frame, IModel model, IController controller, int board) {
        addMouseListener(this);
        int sW = frame.getWidth() / 8;
        int sH = frame.getHeight() / 8;
        this.model = model;
        this.controller = controller;
        this.player = player;
        this.current = frame;
        this.xPos = i;
        this.yPos = j;
        this.board = board;
        this.setPreferredSize(new Dimension(sW, sH));

        // Fill Oval to fill the oval
        // Draw Oval to draw the boarder for the oval

    }

    protected void paintComponent(Graphics g) {
        //super.paintComponent(arg0);
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() + 1, getHeight() + 1);

        switch (getPlayer()) {
            case 1 -> { // player 1
                g.setColor(Color.WHITE);
                g.fillOval((getWidth() + 4 - getWidth() - 2) / 2, (getHeight() + 4 - getHeight() - 2) / 2, getWidth() - 2, getHeight() - 2);
                g.setColor(Color.BLACK);
                g.drawOval((getWidth() + 4 - getWidth() - 2) / 2, (getHeight() + 4 - getHeight() - 2) / 2, getWidth() - 2, getHeight() - 2);
            }
            case 2 -> { // player 2
                g.setColor(Color.BLACK);
                g.fillOval((getWidth() + 4 - getWidth() - 2) / 2, (getHeight() + 4 - getHeight() - 2) / 2, getWidth() - 2, getHeight() - 2);
                g.setColor(Color.WHITE);
                g.drawOval((getWidth() + 4 - getWidth() - 2) / 2, (getHeight() + 4 - getHeight() - 2) / 2, getWidth() - 2, getHeight() - 2);
            }
            default -> {
            }
            // There is no disk at this square -> Draw nothing
        }

    }

    // Invoked when THIS component has been pressed with a mouse
    // When mouse is clicked must get the current player from Controller and call squareSelected
    // Passing the X and Y coordinates of the current panel clicked

    // ->

    // Square selected checks for
    // end of game
    // Moving any new pieces
    // Updates the model with the new squares

    @Override // Mouse listener object -> Calls squareSelected in Controller
    public void mouseClicked(MouseEvent e) {
        // System.out.println("Clicked me!");
        controller.squareSelected(board, this.xPos, this.yPos);
    }

    // get methods
    public int getPlayer() {
        int disk;
        disk = this.player;
        return disk;
    }

    // Unused Mouse Action Listen implementation
    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
