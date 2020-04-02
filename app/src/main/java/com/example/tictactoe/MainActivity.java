package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button[] mBoardButtons;
    // Various text displayed
    private TextView mInfoTextView;
    // Restart Button
    private Button startButton;
    // Game Over
    Boolean mGameOver;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.button0);
        mBoardButtons[1] = (Button) findViewById(R.id.button1);
        mBoardButtons[2] = (Button) findViewById(R.id.button2);
        mBoardButtons[3] = (Button) findViewById(R.id.button3);
        mBoardButtons[4] = (Button) findViewById(R.id.button4);
        mBoardButtons[5] = (Button) findViewById(R.id.button5);
        mBoardButtons[6] = (Button) findViewById(R.id.button6);
        mBoardButtons[7] = (Button) findViewById(R.id.button7);
        mBoardButtons[8] = (Button) findViewById(R.id.button8);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mGame = new TicTacToeGame();
        startNewGame();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the aaction bar if it is present
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        StringBuilder str=new StringBuilder();
        switch (id) {
            case R.id.setting_hard:
                item.setChecked(true);
                str.append(getResources().getString(R.string.difficulty)).append(getResources().getString(R.string.hard));
                Toast.makeText(this, str.toString(),
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.setting_medium:
                item.setChecked(true);
                str.append(getResources().getString(R.string.difficulty)).append(getResources().getString(R.string.medium));
                Toast.makeText(this, str.toString(),
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.setting_easy:
                item.setChecked(true);
                str.append(getResources().getString(R.string.difficulty)).append(getResources().getString(R.string.easy));
                Toast.makeText(this, str.toString(),
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_statistics:
                Intent i = new Intent(this, PlayerStatistics.class);
                startActivity(i);
                break;
            case R.id.menu_exit:
                finish();
                return true;
        }
        return false;
    }

    //--- Set up the game board.
    private void startNewGame() {
        mGameOver = false;
        mGame.clearBoard();
        //---Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        //---Human goes first
        mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
        mInfoTextView.setText(R.string.player_first);
    }

    //add sound effect
    public void onButtonClicked(View view) {

    }

    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        Timer timer = new Timer();

        ButtonClickListener(int location) {
            this.location = location;
        }
        @Override
        public void onClick(View v) {
            if (!mGameOver&&flag) {
                if (mBoardButtons[location].isEnabled()) {
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    //--- If no winner yet, let the computer make a move
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.Android_turn);
                        int move = mGame.getComputerMove();
                        setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                        winner = mGame.checkForWinner();
                    }
                    if (winner == 0) {
                       flag = false;
                       timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
                               mInfoTextView.setText(R.string.player_turn);
                               flag = true;
                           }
                       },800);


                    } else if (winner == 1) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
                        mInfoTextView.setText(R.string.tie);
                        mGameOver = true;
                    } else if (winner == 2) {
                        mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
                        mInfoTextView.setText(R.string.win);
                        mGameOver = true;
                    } else {
                        mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
                        mInfoTextView.setText(R.string.lose);
                        mGameOver = true;
                    }
                }
            }
        }
    }
    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }
    //--- OnClickListener for Restart a New Game Button
    public void newGame(View v) {
        startNewGame();
    }

}

