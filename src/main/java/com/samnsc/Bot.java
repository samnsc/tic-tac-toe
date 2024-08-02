package com.samnsc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot {
    private final Board board;

    public Bot(Board parentBoard) {
        board = parentBoard;
    }

    public Move getBotMove() {
        List<Move> validMoves = board.getValidMoves();
        Map<Move, Double> pathWeight = new HashMap<>();

        for (Move move : validMoves) {
            Board boardClone = board.clone();
            double result = playGameUntilResult(false, move, boardClone);

            pathWeight.put(move, result);
        }

        Move bestMove = validMoves.getFirst();
        double bestMoveScore = pathWeight.get(validMoves.getFirst());
        for (int i = 1; i < validMoves.size(); i++) {
            Move currentMove = validMoves.get(i);
            double currentMoveScore = pathWeight.get(currentMove);

            if (currentMoveScore > bestMoveScore) {
                bestMove = currentMove;
                bestMoveScore = currentMoveScore;
            }
        }
        return bestMove;
    }

    // this is a recursive function that will try every possible combination of moves until either player
    // has won on that "branch", when that does happen it will either return -201 if the bot loses in that
    // branch, 100 if it wins or 0 if it ties, these values were chosen so that the bot would be discouraged
    // from making a play that would result in 2 win opportunities but would allow the player to instantly win
    // the game in 1 move
    // afterward the returned recursive value is multiplied by 0.2 so that the bot values more
    // the immediate consequences of their play, i.e. the player winning
    // all of these values were chosen experimentally and aren't guaranteed to be perfect, more tinkering might be required
    private double playGameUntilResult(boolean isPlayerOneTurn, Move curMove, Board simulatedBoard) {
        boolean hasWon = simulatedBoard.playTurn(isPlayerOneTurn, curMove);

        if (hasWon) {
            return isPlayerOneTurn ? -250 : 100;
        }

        List<Move> validMoves = simulatedBoard.getValidMoves();
        if (validMoves.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Move move : validMoves) {
            Board boardClone = simulatedBoard.clone();
            sum += (playGameUntilResult(!isPlayerOneTurn, move, boardClone) * 0.2);
        }
        return sum;
    }
}
