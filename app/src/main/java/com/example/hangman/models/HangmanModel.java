package com.example.hangman.models;

import android.os.Build;

import com.google.gson.Gson;

import java.util.Random;

public class HangmanModel {
    private static final Random PICKER = new Random();
    private static final int STARTING_LIVES = 6;
    private static String[] wordList;
    private String word;
    private String[] letters;
    private String[] newLetters;
    private int lives;
    private int letterTracker;
    private int guessTracker;
    private String[] guess;
    private String badGuessMessage;
    private String guessMessage;
    private boolean useShortWords = false;

    public HangmanModel(){
        setWordList();
    }

    public String openingMessage(){
        return "Welcome to Hangman!";
    }

    public String initialShowWordMessage(){
        return "Here is your word:";
    }

    public void setupGame(){
        int wordNum = 0;
        int max = useShortWords ? 8 : 12;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            do {
                wordNum = PICKER.nextInt(0, wordList.length);
            } while (wordList[wordNum].length() > max);
        }
        word = wordList[wordNum];
        letters = new String[word.length()];
        newLetters = new String[letters.length];
        for (int i = 0; i < word.length(); i++) {
            letters[i] = word.substring(i, i + 1);
            newLetters[i] = "_";
        }
        guess = new String[26];
        lives = STARTING_LIVES;
        letterTracker = 0;
        guessTracker = 0;
    }

    public boolean hasGameStarted(){
        return guessTracker > 0;
    }

    public int getLivesRemaining(){
        return lives;
    }

    public boolean isGameDone(){
        return letterTracker >= letters.length || lives <= 0;
    }

    public String livesRemainingMessage(){
        if (lives > 1){
            return "You have " + lives + " lives left";
        } else {
            return "You have 1 life left! Be careful!";
        }
    }

    public String guessALetterMessage(){
        if (guessTracker == 0){
            return "Guess your first letter: ";
        } else {
            return "Guess your next letter: ";
        }
    }

    public boolean guessedValidLetter(String s){
        if (!guessedASingleLetter(s)){
            return false;
        }
        return guessedANewLetter(s);
    }

    private boolean guessedASingleLetter(String s){
        if (s.length() != 1 || !Character.isLetter(s.charAt(0))){
            badGuessMessage = "Invalid guess entered. It must be a single letter. Guess another one";
            return false;
        }
        return true;
    }

    private boolean guessedANewLetter(String s){
        guess[guessTracker] = s;
        for (int i = 0; i < guessTracker; i++) {
            if (guess[guessTracker].equals(guess[i])) {
                badGuessMessage = "You already used this letter. Guess another one";
                return false;
            }
        }
        return true;
    }

    public String getBadGuessMessage(){
        return badGuessMessage;
    }

    public void checkIfLetterIsInWord(){
        boolean change = false;
        for (int j = 0; j < letters.length; j++) {
            if (guess[guessTracker].equals(letters[j])){
                newLetters[j] = guess[guessTracker];
                letterTracker++;
                change = true;
            }
        }

        guessTracker++;

        if (change){
            guessMessage = "Correct!";
        } else {
            lives--;
            guessMessage = "Incorrect";
        }
    }

    public String[] getNewLetters(){
        return newLetters.clone();
    }

    public String getGuessMessage(){
        return guessMessage;
    }

    public String gameOverMessage(){
        if (lives > 0){
            return "Congratulations! You won!";
        } else {
            return  "Unfortunately, you lost. The word was \"" + word + "\"";
        }
    }

    public String closingMessage(){
        return "\nThanks for playing! Have a nice day!";
    }

    public static String getJSONFromGame(HangmanModel obj) {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

    public static HangmanModel getGameFromJSON(String json) {
        Gson gson = new Gson ();
        return gson.fromJson (json, HangmanModel.class);
    }

    public String getJSONFromCurrentGame() {
        return getJSONFromGame(this);
    }

    public void setUseShortWords(boolean useShortWords){
        this.useShortWords = useShortWords;
    }

    private void setWordList(){
        wordList = new String[153];
        wordList[0] = "ability";
        wordList[1] = "acrobatics";
        wordList[2] = "addition";
        wordList[3] = "adventure";
        wordList[4] = "airplane";
        wordList[5] = "airport";
        wordList[6] = "alligator";
        wordList[7] = "alphabet";
        wordList[8] = "apartment";
        wordList[9] = "application";
        wordList[10] = "arbitrary";
        wordList[11] = "arithmetic";
        wordList[12] = "astronaut";
        wordList[13] = "baseball";
        wordList[14] = "basketball";
        wordList[15] = "birthday";
        wordList[16] = "blackberry";
        wordList[17] = "blackboard";
        wordList[18] = "blueberry";
        wordList[19] = "bookcase";
        wordList[20] = "borderline";
        wordList[21] = "boundary";
        wordList[22] = "building";
        wordList[23] = "butterfly";
        wordList[24] = "cabinet";
        wordList[25] = "caboose";
        wordList[26] = "calculator";
        wordList[27] = "calendar";
        wordList[28] = "capitalism";
        wordList[29] = "caterpillar";
        wordList[30] = "cavalry";
        wordList[31] = "ceremony";
        wordList[32] = "chandelier";
        wordList[33] = "character";
        wordList[34] = "characteristics";
        wordList[35] = "chocolate";
        wordList[36] = "communism";
        wordList[37] = "community";
        wordList[38] = "compare";
        wordList[39] = "composer";
        wordList[40] = "comprehend";
        wordList[41] = "comprehensive";
        wordList[42] = "computer";
        wordList[43] = "condition";
        wordList[44] = "confusion";
        wordList[45] = "corridor";
        wordList[46] = "crocodile";
        wordList[47] = "dictionary";
        wordList[48] = "difference";
        wordList[49] = "different";
        wordList[50] = "differential";
        wordList[51] = "dinosaur";
        wordList[52] = "district";
        wordList[53] = "division";
        wordList[54] = "document";
        wordList[55] = "elephant";
        wordList[56] = "elevator";
        wordList[57] = "encyclopedia";
        wordList[58] = "entertainment";
        wordList[59] = "escalator";
        wordList[60] = "essential";
        wordList[61] = "exponent";
        wordList[62] = "fabulous";
        wordList[63] = "fahrenheit";
        wordList[64] = "football";
        wordList[65] = "general";
        wordList[66] = "glossary";
        wordList[67] = "gymnasium";
        wordList[68] = "gymnastics";
        wordList[69] = "hallway";
        wordList[70] = "hangman";
        wordList[71] = "hemisphere";
        wordList[72] = "hippopotamus";
        wordList[73] = "hundred";
        wordList[74] = "illustrate";
        wordList[75] = "illustration";
        wordList[76] = "infection";
        wordList[77] = "invisibility";
        wordList[78] = "invisible";
        wordList[79] = "jellyfish";
        wordList[80] = "keyboard";
        wordList[81] = "knapsack";
        wordList[82] = "language";
        wordList[83] = "locomotive";
        wordList[84] = "looseleaf";
        wordList[85] = "magazine";
        wordList[86] = "magical";
        wordList[87] = "mathematics";
        wordList[88] = "mechanism";
        wordList[89] = "microscope";
        wordList[90] = "million";
        wordList[91] = "monitor";
        wordList[92] = "mountain";
        wordList[93] = "multiplication";
        wordList[94] = "multiply";
        wordList[95] = "mysterious";
        wordList[96] = "mystery";
        wordList[97] = "negative";
        wordList[98] = "notebook";
        wordList[99] = "orchestra";
        wordList[100] = "oriental";
        wordList[101] = "original";
        wordList[102] = "oxymoron";
        wordList[103] = "prescribe";
        wordList[104] = "prescription";
        wordList[105] = "position";
        wordList[106] = "printer";
        wordList[107] = "program";
        wordList[108] = "reinforcements";
        wordList[109] = "religion";
        wordList[110] = "reverence";
        wordList[111] = "secondary";
        wordList[112] = "semicircle";
        wordList[113] = "shampoo";
        wordList[114] = "sidewalk";
        wordList[115] = "skyscraper";
        wordList[116] = "slideshow";
        wordList[117] = "stadium";
        wordList[118] = "staircase";
        wordList[119] = "stomach";
        wordList[120] = "strawberry";
        wordList[121] = "submarine";
        wordList[122] = "subscribe";
        wordList[123] = "subscription";
        wordList[124] = "subtraction";
        wordList[125] = "surgeon";
        wordList[126] = "suspend";
        wordList[127] = "suspension";
        wordList[128] = "symphony";
        wordList[129] = "technological";
        wordList[130] = "technology";
        wordList[131] = "telephone";
        wordList[132] = "telescope";
        wordList[133] = "television";
        wordList[134] = "terminal";
        wordList[135] = "thermometer";
        wordList[136] = "thesaurus";
        wordList[137] = "thorough";
        wordList[138] = "thousand";
        wordList[139] = "trailblazer";
        wordList[140] = "transport";
        wordList[141] = "transportation";
        wordList[142] = "triangle";
        wordList[143] = "underwater";
        wordList[144] = "universe";
        wordList[145] = "university";
        wordList[146] = "velocity";
        wordList[147] = "ventilate";
        wordList[148] = "villager";
        wordList[149] = "visibility";
        wordList[150] = "vocabulary";
        wordList[151] = "wallpaper";
        wordList[152] = "wastebasket";
    }
}