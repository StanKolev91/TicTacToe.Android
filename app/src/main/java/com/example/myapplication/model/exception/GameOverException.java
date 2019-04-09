package com.example.myapplication.model.exception;

public class GameOverException extends Exception{
    private int msgId;

    public GameOverException(int msgId){
        this.msgId = msgId;
    }

    public int getMsgId() {
        return msgId;
    }
}
