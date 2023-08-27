package reversi;

public class ReversiController implements IController {

    IView view;
    IModel model;
    int finalBlackPieces = 0;
    int finalWhitePieces = 0;
    int draw = 0;
    int[][] piecesFlipped = new int[64][2];
    int maxFlipped = 0;


    @Override
    public void initialise(IModel model, IView view) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void startup() {
        // Starts up in mirror position to GUIview startup
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    // creates a white reversi disk on square (i,j)
                    model.setBoardContents(i, j, 1);

                } else if ((i == 3 && j == 4) || (i == 4 && j == 3)) {
                    // creates a black reversi disk on square (i,j)
                    model.setBoardContents(i, j, 2);

                } else {
                    // creates a blank reversi disk on a square(i,j)
                    model.setBoardContents(i, j, 0);
                }
            }
            view.refreshView();

        }

        // White plays first
        model.setPlayer(1);
        model.setFinished(false);
        view.feedbackToUser(1, "White player - choose where to put your piece");
        view.feedbackToUser(2, "Black player - not your turn");
    }


    @Override
    public void update() {
        updatePlayerMessages();

        // Checking if game has finished
        if (hasGameFinished()) {
            // Set appropriate game messages
            if (finalBlackPieces > finalWhitePieces) {
                // Black wins
                view.feedbackToUser(2, "Black won. Black " + finalBlackPieces + " to White " + finalWhitePieces + ". Reset the game to replay.");
                view.feedbackToUser(1, "Black won. Black " + finalBlackPieces + " to White " + finalWhitePieces + ". Reset the game to replay.");
            } else if (finalWhitePieces > finalBlackPieces) {
                // White wins
                view.feedbackToUser(2, "White won. White " + finalWhitePieces + " to Black " + finalBlackPieces + ". Reset the game to replay.");
                view.feedbackToUser(1, "White won. White " + finalWhitePieces + " to Black " + finalBlackPieces + ". Reset the game to replay.");
            } else {
                // Draw
                view.feedbackToUser(2, "Draw. Both players ended with " + finalWhitePieces + " pieces. Reset game to replay.");
                view.feedbackToUser(1, "Draw. Both players ended with " + finalWhitePieces + " pieces. Reset game to replay.");
            }
            view.refreshView();
            return;
        }


        view.refreshView();

    }

    @Override
    public void squareSelected(int player, int j, int k) {
        int flipped = canPlaySquare(player, j, k);

        // Player clicks when not their turn
        if (player != model.getPlayer()) {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }

        // Finished = no change
        if (model.hasFinished()) {
            return;
        }

        // Player clicks when not their turn
        if (player != model.getPlayer()) {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }

        // Controller needs to decide if to put a piece on the board or not
        if (model.getBoardContents(j, k) > 0) {
            // Placing on a square where there is already a piece
            return;
        }

        if (flipped == 0) {
            // Does not capture any pieces of the opponent colour
            return;
        }

        model.setBoardContents(j, k, player);

        for (int t = 0; t < flipped; t++) {
            model.setBoardContents(piecesFlipped[t][0], piecesFlipped[t][1], player);
        }

        // Reset all array index back to 0
        for (int g = 0; g < 64; g++) {
            for (int h = 0; h < 2; h++) {
                piecesFlipped[g][h] = 0;
            }
        }

        // Change player
        if (player == 1) {
            model.setPlayer(2);
        } else if (player == 2) {
            model.setPlayer(1);
        }

        update();

    }

    @Override
    public void doAutomatedMove(int player) {
        int largestX = 0;
        int largestY = 0;
        // Player clicks when not their turn
        if (player != model.getPlayer()) {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }

        // get all squares which do not have a piece on them already
        for (int x = 0; x < model.getBoardWidth(); x++) {
            for (int y = 0; y < model.getBoardHeight(); y++) {
                if (model.getBoardContents(x, y) == 0)// No piece on this square
                {
                    //System.out.println("("+x+" , " + y + ")");
                    int flipped = canPlaySquare(player, x, y);
                    if (flipped > 0) {
                        if (flipped > maxFlipped) {

                            largestX = x;
                            largestY = y;
                            maxFlipped = flipped;
                        }
                    }
                }
            }
        }


        squareSelected(player, largestX, largestY);
        maxFlipped = 0;
    }

    public int canPlaySquare(int player, int x, int y) {
        int xStart = x;
        int yStart = y;
        int opposingDisk = 1;
        int topPiece = 0;       // Reflects how many pieces turned over
        int p = 0;

        if (model.getBoardContents(x,y) != 0){
            // Return as this is occupied by another disk
            return 0;
        }

        model.setBoardContents(x, y, player);     // Temporarily add to the board

        if (player == 1) {
            opposingDisk = 2;
        } else if (player == 2) {
            opposingDisk = 1;
        }

        // X and Y offset
        for (int yOff = -1; yOff <= 1; yOff++) {
            for (int xOff = -1; xOff <= 1; xOff++) {
                if (!(xOff == 0 && yOff == 0)) {
                    xStart = x;
                    yStart = y;
                    yStart += yOff;
                    xStart += xOff;
                    if (onBoard(xStart, yStart) && model.getBoardContents(xStart, yStart) == opposingDisk) {
                        //System.out.println(" main if Entered");
                        while (model.getBoardContents(xStart, yStart) == opposingDisk) {
                            xStart += xOff;
                            yStart += yOff;
                            if (!onBoard(xStart, yStart)) {
                                break;
                            }
                            // System.out.println(" 1st while entered Entered");

                        }
                        if (!onBoard(xStart, yStart)) {
                            continue;
                        }
                        if (model.getBoardContents(xStart, yStart) == player) {
                            //System.out.println(" own disc if entered");

                            while (true) {
                                xStart -= xOff;
                                yStart -= yOff;
                                if ((xStart == x) && (yStart == y)) {
                                    break;
                                }
                                piecesFlipped[p][0] = xStart;
                                piecesFlipped[p][1] = yStart;
                                //System.out.println("Location of opponent disk a: " + piecesFlipped[p][0] +" , " + piecesFlipped[p][1]);
                                p = p + 1;
                                topPiece = p;
                            }
                        }
                    }
                }
            }
        }

        // topPiece = 0 -> Invalid move
        // topPiece > 1 -> valid move

        // Reset chosen space
        model.setBoardContents(x, y, 0);

        return topPiece;
    }


    public boolean onBoard(int x, int y) {
        boolean board;
        board = (x >= 0 && x <= 7) && (y >= 0 && y <= 7);

        return board;

    }

    public boolean hasGameFinished() {
        boolean finished = false;
        int finalB = 0;
        int finalW = 0;
        boolean movePieceP1;
        boolean movePieceP2;
        boolean finishedNoMove = false;
        boolean finishEmpty = true;

        movePieceP1 = makeMove(model.getPlayer());

        if (!movePieceP1) {
            // Change player
            if (model.getPlayer() == 1) {
                model.setPlayer(2);
            } else if (model.getPlayer() == 2) {
                model.setPlayer(1);
            }
        }

        updatePlayerMessages();

        movePieceP2 = makeMove(model.getPlayer());

        if (!movePieceP1 && !movePieceP2) {
            finishedNoMove = true;
        }

        // Check if there is an empty square
        for (int x = 0; x < model.getBoardWidth(); x++) {
            for (int y = 0; y < model.getBoardHeight(); y++) {
                if (model.getBoardContents(x, y) == 0) {
                    finishEmpty = false;
                    model.setFinished(false);
                }
            }
        }

        if (finishEmpty || finishedNoMove || model.hasFinished()) {
            finished = true;
        }

        model.setFinished(finished);

        if (finished) {
            for (int x = 0; x < model.getBoardWidth(); x++) {
                for (int y = 0; y < model.getBoardHeight(); y++) {
                    if (model.getBoardContents(x, y) == 1) {
                        // White disk on square
                        finalW += 1;
                    } else if (model.getBoardContents(x, y) == 2) {
                        // Black disk on square
                        finalB += 1;
                    }
                }
            }
        }

        if (finalB == finalW) {
            this.draw = finalW;
        }

        finalBlackPieces = finalB;
        finalWhitePieces = finalW;

        return finished;
    }


    public boolean makeMove(int player) {
        boolean move = false;

        // get all squares which do not have a piece on them already
        for (int x = 0; x < model.getBoardWidth(); x++) {
            for (int y = 0; y < model.getBoardHeight(); y++) {
                if (model.getBoardContents(x, y) == 0)// No piece
                // e on this square
                {
                    if (canPlaySquare(player, x, y) != 0) {
                        move = true;                                // There is a position on the board that this player can play which at-least turns over 1 other
                    }
                }
            }
        }

        return move;
    }


    public void updatePlayerMessages() {
        // Updating player messages
        if (model.getPlayer() == 1) {
            view.feedbackToUser(1, "White player – choose where to put your piece");
            view.feedbackToUser(2, "Black player – not your turn");
        } else if (model.getPlayer() == 2) {
            view.feedbackToUser(2, "Black player – choose where to put your piece");
            view.feedbackToUser(1, "White player – not your turn");
        }
    }
}
