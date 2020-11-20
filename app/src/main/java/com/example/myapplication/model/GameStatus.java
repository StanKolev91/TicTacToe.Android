package com.example.myapplication.model;

public class GameStatus {

    private int[][] data;
    private int msgId;

    public GameStatus(int[][] data, int msg) {
        this.data = data;
        this.msgId = msg;
    }

    public int[][] getData() {
        return data;
    }

    public int getMsgId() {
        return msgId;
    }

}
