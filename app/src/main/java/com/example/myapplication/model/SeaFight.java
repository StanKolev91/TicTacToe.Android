package com.example.myapplication.model;

import com.example.myapplication.R;
import com.example.myapplication.model.exception.GameOverException;

import java.util.Random;

public class SeaFight {

    private int[][] table;
    private Random rand = new Random();

    private void findBestPosition(int[][] table) throws GameOverException {                        // checks every possible position and compares, writes current AI move on the board
        int bestAiPositionValue = -1000;
        int bestAiPositionRow = -1;
        int bestAiPositionColum = -1;

        for (int i = 0; i < table.length; i++) {

            for (int j = 0; j < table[i].length; j++) {

                if (this.table[i][j] == 2) {
                    this.table[i][j] = 1;
                    int value = minimax(table, 0, false); // recurse with next move = Player move, returns lowest possible value, for every position {highest chance for player win}
                    if (bestAiPositionValue < value) {                    // gets the highest value of all { lowest chance for player win }
                        bestAiPositionValue = value;
                        bestAiPositionRow = i;
                        bestAiPositionColum = j;
                    }
                    this.table[i][j] = 2;
                }
            }
        }

        table[bestAiPositionRow][bestAiPositionColum]= 1;
        checkIfOver();
    }    // checks every possible position and compares, writes the position with lowest player win chance as a current AI move on the board

    private int minimax(int[][] table, int depth, boolean isAiMove) {

        int tableEvaluation = checkForWinAndReturnScore();  // checking for 3 consecutive returns 10,-10,0 {AI wins, Player wins, tie}
        if (tableEvaluation == 10) {
            return tableEvaluation - depth;
        }
        if (tableEvaluation == -10) {
            return tableEvaluation + depth;
        }
        if (tableIsFull()) {
            return 0;
        }
        if (isAiMove) {  //AI move, checks move and returns value - depth, in case of 2 situation with the same result, gets the closest in depth, gets the maximal value of all for AI move
            int bestMove = -1000;

            for (int i = 0; i < table.length; i++) {

                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j] == 2) {
                        table[i][j] = 1;
                        bestMove = max(bestMove, minimax(table, depth + 1, !isAiMove));
                        table[i][j] = 2;
                    }
                }
            }
            return bestMove;
        } else {        //Player move, checks move and returns value + depth, in case of 2 situation with the same result, gets the closest in depth, gets the minimal value of all for Player move
            int bestMove = 1000;

            for (int i = 0; i < table.length; i++) {

                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j] == 2) {
                        table[i][j] = 0;
                        bestMove = min(bestMove, minimax(table, depth + 1, !isAiMove));
                        table[i][j] = 2;
                    }
                }
            }
            return bestMove;
        }
    }  // Minimax Algorithm

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

        for (int i = 0; i < this.table.length; i++) {

            for (int j = 0; j < this.table[i].length; j++) {
                if (table[i][j] == 2) isFull = false;
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

                if (this.table[red][colum] == 1) aiBroiHor++;
                if (this.table[red][colum] == 0) playerBroiHor++;
                if (this.table[colum][red] == 1) aiBroiVer++;
                if (this.table[colum][red] == 0) playerBroiVer++;
            }
            if (aiBroiHor == 3 || aiBroiVer == 3) {
                return 10;
            }
            if (playerBroiHor == 3 || playerBroiVer == 3) {
                return -10;
            }
            aiBroiHor = 0;
            playerBroiHor = 0;
            aiBroiVer = 0;
            playerBroiVer = 0;
        }

        for (int diag = 0; diag < this.table.length; diag++) {
            if (this.table[diag][diag] == 1) aiMainDiag++;
            if (this.table[diag][diag] == 0) playerMainDiag++;
            if (this.table[(this.table.length - 1) - diag][diag] == 1) aiSecDiag++;
            if (this.table[(this.table.length - 1) - diag][diag] == 0) playerSecDiag++;
        }
        if (aiMainDiag == 3 || aiSecDiag == 3) {
            return 10;
        }

        if (playerMainDiag == 3 || playerSecDiag == 3) {
            return -10;
        }
        return 0;
    }  // Evaluation

    private void checkForDraw() throws GameOverException {

        for (int x = 0; x < this.table.length; x++){

            for (int z = 0; z < this.table[0].length ; z++) {
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
        this.table = new int[3][3];
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
        int msg =(R.string.player_turn);
        return new GameStatus(this.table, msg);
    }
}
