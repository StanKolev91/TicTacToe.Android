package com.example.myapplication.model;

import com.example.myapplication.R;
import com.example.myapplication.model.exception.GameOverException;

import java.util.Random;
import java.util.function.BinaryOperator;

public class SeaFight {

    private static final int WIN_MOVE_COUNT = 3;
    private static final int COMPUTER_WINS = 10;
    private static final int PLAYER_WINS = -10;
    private static final int COMPUTER_MOVE = 1;
    private static final int PLAYER_MOVE = 0;
    private static final int EMPTY_VALUE = 2;
    private static final int ITS_A_TIE = 0;

    private int[][] table;
    private final Random rand = new Random();

    // checks every possible position and compares, writes current AI move on the board
    private void findBestPosition(int[][] table) throws GameOverException {
        int bestAiPositionValue = -1000;
        int bestAiPositionRow = -1;
        int bestAiPositionColum = -1;

        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < table[i].length; j++) {

                if (this.table[i][j] == 2) {
                    this.table[i][j] = 1;
                    // recurse with next move = Player move, returns lowest possible value,
                    // for every position {highest chance for player win}
                    int value = minimax(table, 0, false);
                    // gets the highest value of all { lowest chance for player win }
                    if (bestAiPositionValue < value) {

                        bestAiPositionValue = value;
                        bestAiPositionRow = i;
                        bestAiPositionColum = j;
                    }
                    this.table[i][j] = 2;
                }
            }
        }

        table[bestAiPositionRow][bestAiPositionColum] = 1;
        checkIfOver();
    }    // checks every possible position and compares, writes the position with lowest player win chance as a current AI move on the board

    private int minimax(int[][] table, int depth, boolean isAiMove) {
        // checking for 3 consecutive returns 10,-10,0 {AI wins, Player wins, tie}
        int tableEvaluation = checkForWinAndReturnScore();
        if (tableEvaluation == COMPUTER_WINS) {
            return tableEvaluation - depth;
        }
        if (tableEvaluation == PLAYER_WINS) {
            return tableEvaluation + depth;
        }
        if (tableIsFull()) {
            return ITS_A_TIE;
        }
        int bestMove;
        int nextMove;
        BinaryOperator<Integer> comparingFunction;

        if (isAiMove) {
            //AI move, checks move and returns value - depth, in case of 2 situation with the same result,
            // gets the closest in depth, gets the maximal value of all for AI move
            bestMove = Integer.MIN_VALUE;
            nextMove = COMPUTER_MOVE;
            comparingFunction = this::max;
        } else {
            //Player move, checks move and returns value + depth, in case of 2 situation with the same result,
            // gets the closest in depth, gets the minimal value of all for Player move
            bestMove = Integer.MAX_VALUE;
            nextMove = PLAYER_MOVE;
            comparingFunction = this::min;
        }
        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == EMPTY_VALUE) {
                    table[i][j] = nextMove;
                    bestMove = comparingFunction.apply(bestMove, minimax(table, depth + 1, !isAiMove));
                    table[i][j] = EMPTY_VALUE;
                }
            }
        }
        return bestMove;
    }

    private int max(int a, int b) {
        if (a > b) {
            b = a;
        }
        return b;
    }  // returns maximum of 2 number

    private int min(int a, int b) {
        if (a < b) {
            b = a;
        }
        return b;
    }  // returns minimum of 2 number

    private boolean tableIsFull() {
        boolean isFull = true;

        for (int[] ints : this.table) {

            for (int anInt : ints) {
                if (anInt == 2) {
                    isFull = false;
                    break;
                }

            }
        }
        return isFull;
    }  // returns true if no more moves5

    private int checkForWinAndReturnScore() {
        int aiBroiHor = 0;
        int playerBroiHor = 0;
        int aiBroiVer = 0;
        int playerBroiVer = 0;
        int aiMainDiag = 0;
        int playerMainDiag = 0;
        int aiSecDiag = 0;
        int playerSecDiag = 0;

        for (int red = 0; red < this.table.length; red++) {

            for (int colum = 0; colum < this.table[red].length; colum++) {

                if (this.table[red][colum] == COMPUTER_MOVE) aiBroiHor++;
                if (this.table[red][colum] == PLAYER_MOVE) playerBroiHor++;
                if (this.table[colum][red] == COMPUTER_MOVE) aiBroiVer++;
                if (this.table[colum][red] == PLAYER_MOVE) playerBroiVer++;
            }
            if (aiBroiHor == WIN_MOVE_COUNT || aiBroiVer == WIN_MOVE_COUNT) {
                return COMPUTER_WINS;
            }
            if (playerBroiHor == WIN_MOVE_COUNT || playerBroiVer == WIN_MOVE_COUNT) {
                return PLAYER_WINS;
            }
            aiBroiHor = 0;
            playerBroiHor = 0;
            aiBroiVer = 0;
            playerBroiVer = 0;
        }

        for (int diag = 0; diag < this.table.length; diag++) {
            if (this.table[diag][diag] == COMPUTER_MOVE) aiMainDiag++;
            if (this.table[diag][diag] == PLAYER_MOVE) playerMainDiag++;
            if (this.table[(this.table.length - 1) - diag][diag] == COMPUTER_MOVE) aiSecDiag++;
            if (this.table[(this.table.length - 1) - diag][diag] == PLAYER_MOVE) playerSecDiag++;
        }
        if (aiMainDiag == WIN_MOVE_COUNT || aiSecDiag == WIN_MOVE_COUNT) {
            return COMPUTER_WINS;
        }

        if (playerMainDiag == WIN_MOVE_COUNT || playerSecDiag == WIN_MOVE_COUNT) {
            return PLAYER_WINS;
        }
        return ITS_A_TIE;
    }  // Evaluation

    private void checkForDraw() throws GameOverException {

        for (int x = 0; x < this.table.length; x++) {

            for (int z = 0; z < this.table[0].length; z++) {
                if (this.table[x][z] == 2) return;
            }
        }
        throw new GameOverException(R.string.draw);
    }

    private void checkForWin() throws GameOverException {
        if (checkForWinAndReturnScore() == 10) throw new GameOverException(R.string.ai_win);
        if (checkForWinAndReturnScore() == -10) throw new GameOverException(R.string.player_win);
    }

    private void checkIfOver() throws GameOverException {
        checkForWin();
        checkForDraw();
    }

    public GameStatus processMove(Move move) throws GameOverException {
        this.table[move.getX()][move.getY()] = 0;
        checkIfOver();
        findBestPosition(table);
        return new GameStatus(table, R.string.player_turn);
    }

    public GameStatus newGame(Boolean playerFirst) {
        playerFirst = rand.nextBoolean();   //TODO implement 1st move choice button
        this.table = new int[WIN_MOVE_COUNT][WIN_MOVE_COUNT];
        int aiMoveI;
        int aiMoveJ;

        for (int a = 0; a < table.length; a++) {

            for (int b = 0; b < table[a].length; b++) {
                this.table[a][b] = 2;
            }
        }
        if (!playerFirst) {                       // 1st AI move - Random
            aiMoveI = this.rand.nextInt(2 + 1);
            aiMoveJ = this.rand.nextInt(2 + 1);
            this.table[aiMoveI][aiMoveJ] = 1;
        }
        int msg = (R.string.player_turn);
        return new GameStatus(this.table, msg);
    }
}
