package com.example.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.View;

import com.example.hangman.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Objects;
import static com.example.hangman.HangmanModel.getGameFromJSON;
import static com.example.hangman.HangmanModel.getJSONFromGame;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int[] mImages;
    private HangmanModel mGame;
    private final String keyMGame = "GAME_KEY";
    private String[] lettersInWord;
    private StringBuilder formattedWordBuilder;
    private String formattedWord;
    private ImageView mImageViewHangman;
    private Snackbar mSnackBar;
    private boolean useShortWords;
    private String keyShortWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupImagesIntArray();
        mImageViewHangman = findViewById(R.id.image_view_hangman);

        keyShortWords = getString(R.string.short_words_key);

        mSnackBar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG).setAction("Action", null);

        mGame = new HangmanModel();

        SharedPreferences sp = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        useShortWords = sp.getBoolean(keyShortWords, false);

        if (savedInstanceState == null) {
            setupGame();
        }

        binding.contentMain.allContent.guessLetterEditText.fab.setOnClickListener(this::handleFABClick);
    }

    @Override
    protected void onStart() {
        super.onStart();

        restoreOrSetFromPreferences_AllAppAndGameSettings();
    }

    private void setupGame(){
        mGame.setUseShortWords(useShortWords);
        mGame.setupGame();

        if (!mGame.hasGameStarted()){
            mSnackBar.setText(mGame.openingMessage());
            mSnackBar.show();
        }

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(keyMGame, getJSONFromGame(mGame));
        outState.putBoolean(keyShortWords, useShortWords);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        useShortWords = savedInstanceState.getBoolean(keyShortWords);
        String json = savedInstanceState.getString(keyMGame);
        if (json != null) {
            mGame = getGameFromJSON(json);
            mGame.setUseShortWords(useShortWords);
        }
        updateUI();
    }

    private void updateUI() {
        mImageViewHangman.setImageDrawable(ContextCompat.getDrawable(this, mImages[mGame.getLivesRemaining()]));

        lettersInWord = mGame.getNewLetters();
        formattedWordBuilder = new StringBuilder();
        for (String s : lettersInWord){
            formattedWordBuilder.append(" ").append(s).append(" ");
        }
        formattedWord = formattedWordBuilder.toString();

        binding.contentMain.allContent.fullWordViewInclude.tvFullWord.setText(formattedWord);
    }

    private void handleFABClick(View view){
        String message = "";
        if (!mGame.isGameDone()){
            String guessedLetter = Objects.requireNonNull(binding.contentMain.allContent.guessLetterEditText.textBox.getText()).toString();
            boolean guessedValidLetter = mGame.guessedValidLetter(guessedLetter);
            if (!guessedValidLetter){
                message += mGame.getBadGuessMessage();
            } else {
                mGame.checkIfLetterIsInWord();
                try {
                    mImageViewHangman.setImageDrawable(
                            ContextCompat.getDrawable(this, mImages[mGame.getLivesRemaining()]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    message = "Couldn't update image\n";
                }
                message += mGame.getGuessMessage() + "\n" + mGame.livesRemainingMessage();
                lettersInWord = mGame.getNewLetters();
                formattedWordBuilder = new StringBuilder();
                for (String s : lettersInWord){
                    formattedWordBuilder.append(" ").append(s).append(" ");
                }
                formattedWord = formattedWordBuilder.toString();

                binding.contentMain.allContent.guessLetterEditText.textBox.setText("");
                binding.contentMain.allContent.fullWordViewInclude.tvFullWord.setText(formattedWord);
            }
        } else {
            message = "The game is over. Please restart for a new game.";
        }
        mSnackBar.setText(message);
        mSnackBar.show();
        if (mGame.isGameDone()){
            dismissSnackBarIfShown();
            showInfoDialog(this, "Game Over", mGame.gameOverMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettings();
            return true;
        } else if (id == R.id.restart_game) {
            setupGame();
            return true;
        } else if (id == R.id.about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupImagesIntArray(){
        mImages = new int[]{
                R.drawable.lives_left_0, R.drawable.lives_left_1, R.drawable.lives_left_2,
                R.drawable.lives_left_3, R.drawable.lives_left_4, R.drawable.lives_left_5,
                R.drawable.lives_left_6};
    }

    private void showAbout(){
        dismissSnackBarIfShown();
        showInfoDialog(MainActivity.this, "About Hangman",
                "This is an Android App implementation of the classic game Hangman. Enjoy!");
    }

    private void dismissSnackBarIfShown() {
        if (mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }
    }

    private void showInfoDialog(Context context, String strTitle, String strMsg){
        final DialogInterface.OnClickListener listener = (dialog, which) -> dialog.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (context);

        alertDialogBuilder.setTitle (strTitle);
        alertDialogBuilder.setIcon (R.mipmap.ic_launcher);
        alertDialogBuilder.setMessage (strMsg);
        alertDialogBuilder.setCancelable (true);
        alertDialogBuilder.setNeutralButton (context.getString (android.R.string.ok), listener);

        alertDialogBuilder.show ();
    }

    private void showSettings() {
        dismissSnackBarIfShown();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        settingsLauncher.launch(intent);
    }

    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        useShortWords = sp.getBoolean(keyShortWords, false);

        if (mGame != null) {
            mGame.setUseShortWords(useShortWords);
        }
    }

    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> restoreOrSetFromPreferences_AllAppAndGameSettings());
}