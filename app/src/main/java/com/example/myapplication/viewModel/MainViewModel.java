package com.example.myapplication.viewModel;

import com.example.myapplication.R;
import com.example.myapplication.model.GameStatus;
import com.example.myapplication.model.Move;
import com.example.myapplication.model.SeaFight;
import com.example.myapplication.model.exception.GameOverException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//

public class MainViewModel extends ViewModel {

    private SeaFight game;
    private MutableLiveData<GameStatus> liveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> end = new MutableLiveData<>();

    public LiveData<GameStatus> getLiveData() {
        return liveData;
    }

    public MutableLiveData<Boolean> getEnd() {
        return end;
    }

    public void userMoved(Move move){
        try {
            this.liveData.setValue(game.processMove(move));
        } catch (GameOverException e) {
            this.liveData.setValue(new GameStatus(getLiveData().getValue().getData(),e.getMsgId()));
            this.end.setValue(true);
        }
    }

    public void reset(Boolean playerFirst){
        this.liveData.setValue(game.newGame(playerFirst));
        this.end.setValue(false);
    }

    public void init() {
        if (this.game == null){
            this.game = new SeaFight();
            this.reset(true);
        }
    }
}
