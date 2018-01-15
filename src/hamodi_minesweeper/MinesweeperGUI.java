package hamodi_minesweeper;

import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*; //.JFrame, .JLabel, .JPanel, etc
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Ahmed Hamodi
 */
public class MinesweeperGUI extends MouseAdapter {
    JFrame frame;
    JPanel mainCP, secondaryCP, contentPane1, contentPane2, contentPane3,
            contentPane4, contentPane5, winScreen, loseScreen;
    JButton button, resetGame, highscore;
    JComboBox numberOfBombs;
    JLabel label, timer, bombChangerPrompt, displayWinScreen,
            displayLoseScreen, displayBomb;
    private final int LENGTH = 20;
    private final int FPS = 1000;
    private boolean gameOver = false;
    private int timeInSeconds = 0, delay = 0;
    private int numBombs = 60, row, col;
    private int counter = 0;
    private JButton[][] bGrid = new JButton[LENGTH][LENGTH];
    private boolean[][] checkedGrid = new boolean[LENGTH][LENGTH];
    private static Timer time;
    public HighScoreTable hsTable;
    public ArrayList<Integer> scores = new ArrayList<>();
    
    /**
     * Constructor method for MinesweeperGUI
     */
    public MinesweeperGUI(){
        /* Create and set up the frame */
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* Create content panes */
        createContentPanes();
                
        /* create button grid */
        makeButtonGrid();
        
        /* Set up and/or reset checkedGrid */
        makeCheckedGrid();
        
        /* addBombs to button grid */
        addBombs();
        
        /* count the amount of bombs around each square */
        countBombs();
        
        /* Set up timer */
        time = new Timer(FPS, new gameTimer());
        time.start();
        
        /* Set up timer label */
        timer = new JLabel("Timer: " + Integer.toString(timeInSeconds));
        contentPane2.add(timer);
        
        /* Add button to allow user to reset game */
        resetGame = new JButton("Reset Game");
        resetGame.addActionListener(new buttonListener());
        resetGame.setActionCommand("reset");
        contentPane3.add(resetGame);
        
        /* Add button to allow user to reset game */
        highscore = new JButton("Highscores");
        highscore.addActionListener(new buttonListener());
        highscore.setActionCommand("highscore");
        contentPane4.add(highscore);
        
        /* Set up changing of bombs label */
        bombChangerPrompt = new JLabel();
        bombChangerPrompt.setText("# of Bombs: ");
        bombChangerPrompt.setForeground(Color.black);
        bombChangerPrompt.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        contentPane5.add(bombChangerPrompt);
        
        /* Set up a comboBox to set up number of bombs */
        String[] presetChoices = {"20", "30", "40", "50", "60", "70", "80", "90"};
        //option of 1 for testing purposes
//        String[] presetChoices = {"1", "20", "30", "40", "50", "60", "70", "80", "90"};
        numberOfBombs = new JComboBox(presetChoices);
        numberOfBombs.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        numberOfBombs.setSelectedIndex(3);
        numberOfBombs.addActionListener(new comboBoxListener());
        numberOfBombs.setBackground(Color.lightGray);
        numberOfBombs.setForeground(Color.black);
        contentPane5.add(numberOfBombs);
        
        /* set up win screen and lose screen content panes */
        displayWinScreen = new JLabel();
        ImageIcon icon = new ImageIcon("winScreen.png");
        displayWinScreen.setIcon(icon);
        winScreen.add(displayWinScreen);
        
        displayLoseScreen = new JLabel();
        ImageIcon icon2 = new ImageIcon("loseScreen.png");
        displayLoseScreen.setIcon(icon2);
        loseScreen.add(displayLoseScreen);
        
        /* Add content panes to the main content pane */
        mainCP.add(contentPane1);
        mainCP.add(secondaryCP);
        secondaryCP.add(contentPane2);
        secondaryCP.add(contentPane3);
        secondaryCP.add(contentPane4);
        secondaryCP.add(contentPane5);
        
        /* Add content pane to frame */
        frame.setContentPane(mainCP);
        
        /* Size and then display the frame. */
        frame.setResizable(false);
        frame.setSize(1200, 985);
        frame.setVisible(true);
    }
    
    /**
     * Helper method that creates content panes for the minesweeper game
     * pre: none
     * post: content panes created
     */
    private void createContentPanes() {
        /* Create the main content pane */
        mainCP = new JPanel();
        mainCP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainCP.setBackground(Color.darkGray);
        mainCP.setLayout(new FlowLayout());
        
        /* Create the main content pane */
        secondaryCP = new JPanel();
        secondaryCP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        secondaryCP.setBackground(Color.black);
        secondaryCP.setLayout(new BoxLayout(secondaryCP, BoxLayout.PAGE_AXIS));
        
        /* Create a content pane with a GridLayout and
        empty borders */
        contentPane1 = new JPanel();
        contentPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane1.setBackground(Color.black);
        contentPane1.setLayout(new GridLayout(LENGTH, 0, 0, 0));
        
        /* Create a content pane with a FlowLayout and
        empty borders*/
        contentPane2 = new JPanel();
        contentPane2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane2.setBackground(Color.lightGray);
        contentPane2.setLayout(new FlowLayout());
        
        /* Create a content pane with a FlowLayout and
        empty borders*/
        contentPane3 = new JPanel();
        contentPane3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane3.setBackground(Color.lightGray);
        contentPane3.setLayout(new FlowLayout());
        
        /* Create a content pane with a FlowLayout and
        empty borders*/
        contentPane4 = new JPanel();
        contentPane4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane4.setBackground(Color.lightGray);
        contentPane4.setLayout(new FlowLayout());
        
        /* Create a content pane with a FlowLayout and
        empty borders*/
        contentPane5 = new JPanel();
        contentPane5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane5.setBackground(Color.lightGray);
        contentPane5.setLayout(new FlowLayout());
        
        /* Create win screen and lose screen contentPanes */
        winScreen = new JPanel();
        winScreen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        loseScreen = new JPanel();
        loseScreen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    /**
     * Helper method that creates the high score table
     * pre: none
     * post: high score table created
     */
    private void createHighScoreTable() {
        //ensures that the file is only read on the initial boot up
        if (counter == 0) {
            getScores();
            counter ++;
        }
        
        //creates a new high score table class
        hsTable = new HighScoreTable(scores);
    }
    
    /**
     * Helper method that gets scores from the file stored in storage
     * pre: none
     * post: scores retrieved from file in storage
     */
    private void getScores() {
        //creates the required items that are needed to read the scores file
        File dataFile = new File("scores.txt");
        FileReader in;
        BufferedReader readFile;
        String score;
        
        //checks if the file exists before attempting a read
        if (dataFile.exists()) {
            try {
                in = new FileReader(dataFile);
                readFile = new BufferedReader(in);
                
                //adds lines from the score to the scores array list
                while ((score = readFile.readLine()) != null ) {
                    scores.add(Integer.parseInt(score));
                }
                
                readFile.close();
                in.close();
            } catch (IOException e) {
                System.out.println("Problem reading file.");
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }
    
    /**
     * Helper method to make the button grid
     * pre: none
     * post: button grid made
     */
    private void makeButtonGrid() {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                button = new JButton();
                button.setPreferredSize(new Dimension(45, 45));
                //sets foreground and background color to gray
                //basically hiding the text from the user
                button.setBackground(Color.gray);
                button.setForeground(Color.gray);
                button.addMouseListener(this);
                //adds button to the button grid, and the contentPane1
                bGrid[i][j] = button;
                contentPane1.add(bGrid[i][j]);
            }
        }
    }
    
    /**
     * Helper method that makes a new button grid when game is reset
     * pre: none
     * post: new button grid made
     */
    private void makeNewButtonGrid() {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                //resets all buttons to their default colours and text
                bGrid[i][j].setBackground(Color.gray);
                bGrid[i][j].setForeground(Color.gray);
                bGrid[i][j].setText("  ");
            }
        }
    }
    
    /**
     * Helper method that makes a boolean 2D array
     * pre: none
     * post: boolean 2D array made
     */
    private void makeCheckedGrid() {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                //creates (or resets) the boolean 2D array, checkedGrid
                checkedGrid[i][j] = false;
            }
        }
    }
    
    /**
     * Helper method to add bombs to the button grid
     * pre: none
     * post: bombs added to the button grid
     */
    private void addBombs() {
        int randNum1, randNum2;
        for (int i = 0; i < numBombs; i++) {
            Random rand = new Random();
            //generates the x and y position of the bombs
            randNum1 = rand.nextInt(LENGTH);
            randNum2 = rand.nextInt(LENGTH);
            
            //checks if the x and y position had a bomb there previously
            if (!"B".equals(bGrid[randNum1][randNum2].getText())) {
                //if not, it puts a bomb on that square
                bGrid[randNum1][randNum2].setText("B");
            } else {
                //if so, then it reduces an iteration from the for loop
                i--;
            }
        }
    }
    
    /**
     * Helper method to count the number of bombs around each item in button grid
     * pre: none
     * post: neighboring bombs counted
     */
    private void countBombs() {
        int nBombs;
        for (int rowCount = 0; rowCount < LENGTH; rowCount++) {
            for (int colCount = 0; colCount < LENGTH; colCount++) {
                //calls a helper method to count the bombs beside each square
                nBombs = countBombsBeside(rowCount, colCount);
                
                switch (nBombs) {
                    //based on the returned value, the text is set on the square
                    case 0:
                        bGrid[rowCount][colCount].setText("  ");
                        break;
                    case -1:
                        bGrid[rowCount][colCount].setText("B");
                        break;
                    default:
                        bGrid[rowCount][colCount].setText(Integer.toString(nBombs));
                        break;
                }
            }
        }
    }
    
    /**
     * Helper method to count the number of bombs around each item in button grid
     * pre: none
     * post: neighboring bombs counted
     */
    private int countBombsBeside(int cellRow, int cellCol) {
        //if no bombs found, the default value of 0 is returned
        int bombCount = 0;
        
        //if a bomb is present, a -1 is returned
        if (bGrid[cellRow][cellCol].getText().equals("B")) { //is alive
            return -1;
        }
        
        for (int i = cellRow-1; i <= cellRow+1; i++) {
            for (int j = cellCol-1; j <= cellCol+1; j++) {
                try {
                    //if a bomb is found, the bombCount variable goes up one
                    if (bGrid[i][j].getText().equals("B")) {
                        bombCount ++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.err.println(e.getMessage());
                }
            }
        }
        return bombCount;
    }
    
    /**
     * Helper method that checks if game is over by checking each square and
     * adding up a counter to check if every square is correct
     * pre: none
     * post: count of correct squares is returned
     */
    private int gameOver() {
        int squaresCorrect = 0;
        
        //checks every position in the grid to check if it is correctly identified
        for (int rowCheck = 0; rowCheck < LENGTH; rowCheck++) {
            for (int colCheck = 0; colCheck < LENGTH; colCheck++) {
                //checks if the square has been clicked, or is a bomb
                if (bGrid[rowCheck][colCheck].getBackground().equals(Color.white) ||
                        bGrid[rowCheck][colCheck].getText().equals("B")) {
                    squaresCorrect ++;
                }
            }
        }
        
        //if every square is correct, then the integer (LENGTH * LENGTH) is returned
        //if not, then the amount of correct squares is returned
        if (squaresCorrect == LENGTH * LENGTH) {
            gameOver = true;
            return LENGTH * LENGTH;
        } else {
            return squaresCorrect;
        }
    }
    
    /**
     * Helper method that displays all bombs when game is over
     * pre: none
     * post: bombs are displayed
     */
    private void showAllBombs() {
        for (int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                //displays all bombs to the user before ending the game
                if (bGrid[i][j].getText().equals("B")) {
                    bGrid[i][j].setBackground(Color.black);
                    
                    //if (LENGTH * LENGTH) is achieved, then the text on the bombs
                    //is going to be displayed as green. If not, then the text
                    //on the bombs is going to be displayed as red.
                    if (gameOver() == LENGTH * LENGTH) {
                        bGrid[i][j].setForeground(Color.green);
                    } else {
                        bGrid[i][j].setForeground(Color.red);
                    }
                }
            }
        }
    }

    /**
     * Deals with the mouse being pressed and changes the color
     * of the button based on it's previous state
     * pre: none
     * post: button changed color
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        button = (JButton)e.getComponent();
        String value = button.getText();
        
        //calculates the x and y position of the square the mouse clicked
        //NOTE: This code is hardcoded to work for a 20 by 20 grid.
        //      if grid is changed, these values will need to be changed!
        row = (e.getYOnScreen() - 55) / 45;
        col = (e.getXOnScreen() - 60) / 45;
//        System.out.println("row: " + row);
//        System.out.println("col: " + col);

        //if structure that checks if left or right mouse button is clicked
        if (SwingUtilities.isLeftMouseButton(e)) {
//            System.out.println("left mouse click");
            if ((button.getBackground().equals(Color.gray) ||
                button.getBackground().equals(Color.blue)) && !gameOver) {
                button.setBackground(Color.white);
                button.setForeground(Color.black);
                if (value.equals("B")) {
                    //if bomb is clicked, expose the bomb and end game
                    bGrid[row][col].setBackground(Color.black);
                    bGrid[row][col].setForeground(Color.red);
                    gameOver = true;
                } else if (value.equals("  ")) {
                    //call a depth first search to get all the adjacent zeroes
                    openZeros(row,col);
                }
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            //flagging process is completed below
            if (!(bGrid[row][col].getBackground() == Color.white) && !(gameOver)) {
    //            System.out.println("right mouse click");
                if (bGrid[row][col].getBackground() == Color.blue) {
                    bGrid[row][col].setBackground(Color.gray);
                    bGrid[row][col].setForeground(Color.gray);
                } else {
                    bGrid[row][col].setBackground(Color.blue);
                    bGrid[row][col].setForeground(Color.blue);
                }
            }
        }
    }
    
    /**
     * Helper method for mousePressed (recursive method)
     * pre: none
     * post: opens up every adjacent zero and the respective borders
     * @param x
     * @param y 
     */
    private void openZeros(int x, int y) {
        try {
            if (bGrid[x][y].getText().equals("B") || checkedGrid[x][y] == true) {
                return;
            }
            if (!bGrid[x][y].getText().equals("  ")) { //non 0
                bGrid[x][y].setBackground(Color.white);
                bGrid[x][y].setForeground(Color.black);
            } else if (bGrid[x][y].getText().equals("  ")) {
                bGrid[x][y].setBackground(Color.white);
                bGrid[x][y].setForeground(Color.black);
                checkedGrid[x][y] = true;
                //adjacent cells
                openZeros(x, y-1);
                openZeros(x-1, y); 
                openZeros(x+1, y);
                openZeros(x, y+1);
                //diagonal cells
                openZeros(x-1, y-1);
                openZeros(x+1, y-1);
                openZeros(x-1, y+1);
                openZeros(x+1, y+1);
            }
            else {
                bGrid[x][y].setBackground(Color.white);
                bGrid[x][y].setForeground(Color.black);
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }
    
    /**
     * Runs the GUI
     * pre: none
     * post: none
     */
    public static void runGUI () {
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        MinesweeperGUI project = new MinesweeperGUI();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MinesweeperGUI::runGUI);
    }
    
    /**
     * Deals with the button commands by the user
     * @author Ahmed Hamodi
     */
    public class buttonListener implements ActionListener {
        /**
         * Action Listener for the buttons. Deals with various actions the
         * user can perform. This includes restarting the game and creating
         * the high score table
         * pre: none
         * post: user action performed
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String eventName = e.getActionCommand();

            switch (eventName) {
                case "reset":
                    //resets timer
                    timeInSeconds = 0;
                    timer.setText("Timer: " + Integer.toString(timeInSeconds));
                    time.restart();
                    gameOver = false;
                    delay = 0;

                    /* create button grid */
                    makeNewButtonGrid();

                    /* Set up and/or reset checkedGrid */
                    makeCheckedGrid();

                    /* addBombs to button grid */
                    addBombs();

                    /* count the amount of bombs around each square */
                    countBombs();

                    //removes win or lose screen, when reset is called
                    if (mainCP.getComponent(0).equals(winScreen)) {
                        mainCP.remove(winScreen);
                        mainCP.remove(secondaryCP);
                        mainCP.add(contentPane1);
                        mainCP.add(secondaryCP);
                        frame.setContentPane(mainCP);
                    } else {
                        mainCP.remove(loseScreen);
                        mainCP.remove(secondaryCP);
                        mainCP.add(contentPane1);
                        mainCP.add(secondaryCP);
                        frame.setContentPane(mainCP);
                    }
                    break;
                case "highscore":
                    createHighScoreTable();
                    break;
            }
        }
    }

    /**
     * Deals with comboBox commands by the user
     * @author Ahmed Hamodi
     */
    public class comboBoxListener implements ActionListener {

        /**
         * Method that responds to the user input for the number of bombs
         * on the minesweeper board. It changes the number of bombs on the
         * field, and changes some of the content panes that are on the screen
         * pre: 
         * post: 
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox comboBox = (JComboBox)e.getSource();
            String eventName = (String)comboBox.getSelectedItem();
            
            numBombs = Integer.parseInt(eventName);
            
            //removes win or lose screen, if present, when a new bomb setting is picked
            if (mainCP.getComponent(0).equals(winScreen)) {
                mainCP.remove(winScreen);
                mainCP.remove(secondaryCP);
                mainCP.add(contentPane1);
                mainCP.add(secondaryCP);
                frame.setContentPane(mainCP);
            } else if (mainCP.getComponent(0).equals(loseScreen)) {
                mainCP.remove(loseScreen);
                mainCP.remove(secondaryCP);
                mainCP.add(contentPane1);
                mainCP.add(secondaryCP);
                frame.setContentPane(mainCP);
            }
            
            //resets timer when new bomb setting is picked
            timeInSeconds = 0;
            timer.setText("Timer: " + Integer.toString(timeInSeconds));
            time.restart();
            gameOver = false;
            delay = 0;

            /* create button grid */
            makeNewButtonGrid();

            /* Set up and/or reset checkedGrid */
            makeCheckedGrid();

            /* addBombs to button grid */
            addBombs();

            /* count the amount of bombs around each square */
            countBombs();
        }
    }
    
    /**
     * Deals with the timer and the logic of the game being applied
     * @author Ahmed Hamodi
     */
    class gameTimer implements ActionListener {

        /**
         * Performs the logic of checking and displaying when the game is over,
         * alongside maintaining GUI position on the screen
         * pre: none
         * post: checks and displays when the game is over
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int numCorrect = gameOver();
            
            //if game is over, and it is because of a win condition,
            //bombs are shown and 3 seconds later the win screen is shown
            if (gameOver == true && numCorrect == LENGTH * LENGTH) {
                showAllBombs();
                delay ++;
                if (delay == 3) {
                    scores.add(timeInSeconds);
                    time.stop();
                    mainCP.remove(contentPane1);
                    mainCP.remove(secondaryCP);
                    mainCP.add(winScreen);
                    mainCP.add(secondaryCP);
                    frame.setContentPane(mainCP);
                    delay = -1;
                }
            //if game is over but not in a win condition, then
            //bombs are shown and 3 seconds later the lose screen is shown
            } else if (gameOver == true) {
                showAllBombs();
                delay ++;
                if (delay == 3) {
                    time.stop();
                    mainCP.remove(contentPane1);
                    mainCP.remove(secondaryCP);
                    mainCP.add(loseScreen);
                    mainCP.add(secondaryCP);
                    frame.setContentPane(mainCP);
                    delay = -1;
                }
            }
            
            //if the game is not over, delay is always going to equal 0
            //therefore the timer continues as normal, going up 1 each second
            if (delay == 0) {
                timeInSeconds ++;
                timer.setText("Timer: " + Integer.toString(timeInSeconds));
            }
            
            /* Since I use:
            row = (e.getYOnScreen() - 55) / 45;
            col = (e.getXOnScreen() - 70) / 45;
            it is dependent on the minesweeper game to be at the position (0,0).
            As such, I constantly set the frame to that (0, 0) location in the
            timer as this code is run every second the game is open. */
            frame.setLocation(0, 0);
        }
    }
}