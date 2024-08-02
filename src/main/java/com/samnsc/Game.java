package com.samnsc;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private final Board gameBoard;
    private final char playerOneSymbol;
    private final char playerTwoSymbol;
    private final boolean playerOneFirst;
    private Bot gameBot;

    public Game(boolean playerTwoBot, boolean playerOneFirst, char playerOneSymbol, char playerTwoSymbol) {
        gameBoard = new Board();

        if (playerTwoBot) {
            gameBot = new Bot(gameBoard);
        }

        this.playerOneFirst = playerOneFirst;
        this.playerOneSymbol = playerOneSymbol;
        this.playerTwoSymbol = playerTwoSymbol;
    }

    public void startGame() {
        boolean hasWon = false;
        boolean isPlayerOneTurn = playerOneFirst;

        while (!hasWon && !gameBoard.getValidMoves().isEmpty()) {
            Move currentMove;

            if (!isPlayerOneTurn && gameBot != null) {
                currentMove = gameBot.getBotMove();
                System.out.println("O jogador " + playerTwoSymbol + " (bot) jogou: (" + currentMove.row() + "," + currentMove.column() + ")");
            } else {
                currentMove = getPlayerMove(isPlayerOneTurn);
            }

            try {
                hasWon = gameBoard.playTurn(isPlayerOneTurn, currentMove);
                gameBoard.printBoard(playerOneSymbol, playerTwoSymbol);

                isPlayerOneTurn = !isPlayerOneTurn;
            } catch (InputMismatchException ignore) {
                // this exception is only thrown so that the game board won't be printed
                // and the current players turn won't be flipped on an invalid move,
                // and it's only caught so that the game won't crash
            }
        }

        if (hasWon) {
            // since the player's turn is flipped before it reaches this point I have to flip it back to get the player that actually won
            System.out.println("O jogador " + (!isPlayerOneTurn ? playerOneSymbol : playerTwoSymbol) + " ganhou!");
        } else {
            System.out.println("Empate");
        }

        System.out.println("Jogadas:");
        gameBoard.printPlayedMoves(playerOneFirst, playerOneSymbol, playerTwoSymbol);
    }

    private Move getPlayerMove(boolean isPlayerOneTurn) {
        int row = -1;
        int column = -1;

        Scanner scanner = new Scanner(System.in);

        while (row < 0 || row > 2 || column < 0 || column > 2) {
            System.out.print("Jogador " + (isPlayerOneTurn ? playerOneSymbol : playerTwoSymbol) + " digite a sua jogada (linha,coluna): ");

            String input = scanner.nextLine();

            try {
                if (input.length() != 5 || input.charAt(0) != '(' || input.charAt(2) != ',' || input.charAt(4) != ')') {
                    throw new InputMismatchException();
                }

                int tempRow = Integer.parseInt(String.valueOf(input.charAt(1)));
                int tempColumn = Integer.parseInt(String.valueOf(input.charAt(3)));

                row = tempRow;
                column = tempColumn;
            } catch (NumberFormatException ignored) {
                // ignore this exception because if an invalid char is sent row
                // and column will still be invalid values
                // and the while loop will run again
                // both of these exceptions are only thrown so that row and column
                // aren't set to invalid values
            } catch (InputMismatchException ignored) {
                // also ignore this exception for the same reason
            }
        }

        return new Move(row, column);
    }
}
