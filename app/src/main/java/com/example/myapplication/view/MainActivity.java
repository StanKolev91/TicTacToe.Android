package com.example.myapplication.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.GameStatus;
import com.example.myapplication.model.Move;
import com.example.myapplication.viewModel.MainViewModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Integer TOP_LEFT_BUTTON_ID = 0;
    private static final Integer TOP_MIDDLE_BUTTON_ID = 1;
    private static final Integer TOP_RIGHT_BUTTON_ID = 2;
    private static final Integer MIDDLE_LEFT_BUTTON_ID = 3;
    private static final Integer CENTER_BUTTON_ID = 4;
    public static final int MIDDLE_RIGHT_BUTTON_ID = 5;
    public static final int BOTTOM_LEFT_BUTTON_ID = 6;
    public static final int BOTTOM_MIDDLE_BUTTON_ID = 7;
    public static final int BOTTOM_RIGHT_BUTTON_ID = 8;
    private HashMap<Button, Integer> buttons;
    private TextView info;
    private MainViewModel viewModel;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons = new HashMap<>();
        this.info = findViewById(R.id.textView);
        this.info.setTextSize(26);
        //TODO set text for first turn
        Button resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener((View v) -> {
            viewModel.reset(true);
        });

        Button one = findViewById(R.id.button);
        one.setOnClickListener(this);
        buttons.put(one, TOP_LEFT_BUTTON_ID);

        Button two = findViewById(R.id.button2);
        two.setOnClickListener(this);
        buttons.put(two, TOP_MIDDLE_BUTTON_ID);

        Button three = findViewById(R.id.button3);
        three.setOnClickListener(this);
        buttons.put(three, TOP_RIGHT_BUTTON_ID);

        Button four = findViewById(R.id.button4);
        four.setOnClickListener(this);
        buttons.put(four, MIDDLE_LEFT_BUTTON_ID);

        Button five = findViewById(R.id.button5);
        five.setOnClickListener(this);
        buttons.put(five, CENTER_BUTTON_ID);

        Button six = findViewById(R.id.button6);
        six.setOnClickListener(this);
        buttons.put(six, MIDDLE_RIGHT_BUTTON_ID);

        Button seven = findViewById(R.id.button7);
        seven.setOnClickListener(this);
        buttons.put(seven, BOTTOM_LEFT_BUTTON_ID);

        Button eight = findViewById(R.id.button8);
        eight.setOnClickListener(this);
        buttons.put(eight, BOTTOM_MIDDLE_BUTTON_ID);

        Button nine = findViewById(R.id.button9);
        nine.setOnClickListener(this);
        buttons.put(nine, BOTTOM_RIGHT_BUTTON_ID);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveData().observe(this, result -> {
            for (Button b : buttons.keySet()) {
                updateButton(result, b);
            }
            this.info.setText(result.getMsgId());
        });
        viewModel.getEnd().observe(this, end -> {

            for (Button b : this.buttons.keySet()) {
                if (end) {
                    b.setEnabled(false);
                } else b.setEnabled(true);
            }
        });
        viewModel.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateButton(GameStatus result, Button b) {
        if (buttons != null) {
            Move move = findButtonCoordinates(buttons.get(b));
            Drawable background;
            switch (result.getData()[move.getX()][move.getY()]) {
                case 0:
                    background = getResources().getDrawable(android.R.drawable.btn_radio);
                    break;
                case 1:
                    background = getResources().getDrawable(android.R.drawable.btn_dialog);
                    break;
                default:
                    background = getResources().getDrawable(android.R.drawable.btn_default);
            }
            b.setBackground(background);
        }
    }

    private Move findButtonCoordinates(Integer buttonId) {
        int x = buttonId / 3;
        int y = buttonId - (x * 3);
        return new Move(x, y);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Button clickedButton = ((Button) v);
        Move move = findButtonCoordinates(buttons.get(clickedButton));
        if (viewModel.getLiveData().getValue().getData()[move.getX()][move.getY()] == 2) {
            viewModel.userMoved(move);
        } else info.setText(R.string.invalid_move);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            for (Button b : buttons.keySet()) {
                this.unregisterForContextMenu(b);
            }
        }
    }
}
