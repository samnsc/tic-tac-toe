package com.samnsc;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Board implements Cloneable {
    private int[][] gameBoard;
    private List<Move> validMoves;
    private List<Move> playedMoves;

    public Board() {
        gameBoard = new int[3][3];

        // initialize with all possible moves
        validMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                validMoves.add(new Move(i, j));
            }
        }

        playedMoves = new ArrayList<>();
    }

    public void printBoard(char playerOneSymbol, char playerTwoSymbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (gameBoard[i][j]) {
                    case 0:
                        System.out.print(" ");
                        break;
                    case 1:
                        System.out.print(playerOneSymbol);
                        break;
                    case 2:
                        System.out.print(playerTwoSymbol);
                        break;
                }

                if (j != 2) {
                    System.out.print(" | ");
                }
            }

            // this passes to the next line since the previous print statements all happen on the same line
            System.out.println();
            if (i != 2) {
                System.out.println("---------");
            }
        }
    }

    public boolean playTurn(boolean isPlayerOneTurn, Move move) {
        if (!checkMoveValidity(move)) {
            throw new InputMismatchException("Invalid move");
        }

        gameBoard[move.row()][move.column()] = isPlayerOneTurn ? 1 : 2;
        // records are compared by value and not by reference which is why this works
        playedMoves.add(move);
        validMoves.remove(move);

        return hasWon(move, isPlayerOneTurn ? 1 : 2);
    }

    public List<Move> getValidMoves() {
        return validMoves;
    }

    public void printPlayedMoves(boolean playerOneFirst, char playerOneSymbol, char playerTwoSymbol) {
        for (int i = 0; i < playedMoves.size(); i++) {
            boolean playerOneTurn = (playerOneFirst ? i % 2 == 0 : i % 2 == 1);
            Move currentMove = playedMoves.get(i);

            System.out.println("Jogador " + (playerOneTurn ? playerOneSymbol : playerTwoSymbol) + " jogada " + ((i / 2) + 1) + ": Fileira " + currentMove.row() + "; Coluna " + currentMove.column());
        }
    }

    private boolean checkMoveValidity(Move move) {
        boolean isValid = false;

        for (Move validMove : validMoves) {
            if (validMove.equals(move)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    private boolean hasWon(Move lastMove, int playerNumber) {
        // this is an optimized function that only checks the relevant spaces based on the last move instead
        // of checking every possible combination for 3-in-a-row

        // the two if statements below check for vertical and horizontal wins respectively

        /*
        will be true in situations like this one
        X |   | O |
        -----------
        X |   |   |
        -----------
        X |   | O |
         */
        if (gameBoard[0][lastMove.column()] == playerNumber && gameBoard[1][lastMove.column()] == playerNumber && gameBoard[2][lastMove.column()] == playerNumber) {
            return true;
        }

        /*
        will be true in situations like this one
        X | X | X |
        -----------
          |   |   |
        -----------
        O |   | O |
         */
        if (gameBoard[lastMove.row()][0] == playerNumber && gameBoard[lastMove.row()][1] == playerNumber && gameBoard[lastMove.row()][2] == playerNumber) {
            return true;
        }

        // these will check for diagonal wins
        if (lastMove.column() == 1 && lastMove.row() == 1) { // special case for center space
            return (gameBoard[0][2] == playerNumber && gameBoard[2][0] == playerNumber) ||
                    (gameBoard[0][0] == playerNumber && gameBoard[2][2] == playerNumber);
        } else if ((lastMove.column() == 0 || lastMove.column() == 2) && (lastMove.row() == 0 || lastMove.row() == 2)) { // special case for corner spaces
            // this will separate the 4 corners into two groups in which both members of each group face each other diagonally
            if (lastMove.column() - lastMove.row() == 0) {
                return (gameBoard[0][0] == playerNumber && gameBoard[1][1] == playerNumber && gameBoard[2][2] == playerNumber);
            } else {
                return (gameBoard[2][0] == playerNumber && gameBoard[1][1] == playerNumber && gameBoard[0][2] == playerNumber);
            }
        }

        // this will only be reached by the spaces between two corner spaces that aren't the
        // center space and the game hasn't yet been won
        return false;
    }

    @Override
    public Board clone() {
        try {
            Board clone = (Board) super.clone();

            clone.gameBoard = new int[3][3];
            for (int i = 0; i < 3; i++) {
                System.arraycopy(gameBoard[i], 0, clone.gameBoard[i], 0, 3);
            }


            clone.validMoves = new ArrayList<>(validMoves);
            clone.playedMoves = new ArrayList<>(playedMoves);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
