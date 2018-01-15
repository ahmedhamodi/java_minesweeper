package hamodi_minesweeper;

import java.awt.Color;
import javax.swing.*; //.JFrame, .JLabel, .JPanel, etc
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Ahmed Hamodi
 */
public class HighScoreTable {
    
    private ArrayList<Comparable> scores = new ArrayList<>();
    private JLabel[] finalScores;
    private int length;
    JFrame frame;
    JPanel pane;
    JLabel highscore;
    
    /**
     * Constructor to create a high score table with scores passed to it
     * @param tempScores 
     */
    public HighScoreTable(ArrayList<Integer> tempScores) {
        /* Deal with the scores given to the program */
        length = tempScores.size();
        finalScores = new JLabel[length];
        
        /* copy the arraylist into a class wide arraylist */
        for (int i = 0; i < tempScores.size(); i++) {
            scores.add(tempScores.get(i));
        }
        
        /* Set up the frame */
        frame = new JFrame("Highscores");
        
        /* Create the main content pane */
        pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.setBackground(Color.lightGray);
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        
        /* Set up title for high scores */
        highscore = new JLabel();
        highscore.setText("High Scores: ");
        highscore.setForeground(Color.black);
        highscore.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pane.add(highscore);
        
        /* Initialize values and position for the 10 elements in scores */
        createList();
        
        /* Create file for storing scores, if not previously existing */
        createFile();
        
        /* Sort the top 10 fastest scores */
        sortScores();
        
        /* Update the Label scores via the String scores array */
        updateScores();

        /* Save scores to memory */
        deleteCurrentScores(); //remove current storage of scores
        saveScores(); //store scores into file from memory
        
        /* Add content pane to frame */
        frame.setContentPane(pane);
        
        /* Size and then display the frame. */
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocation(1200, 385);
        frame.setSize(150, 225);
    }
    
    /**
     * Helper method that creates the top 10 list
     * pre: none
     * post: top 10 list is created
     */
    private void createList() {
        for (int i = 0; i < length; i++) {
            if (i < 10) {
                //creates labels to keep track of high score list. maxes out at 10
                finalScores[i] = new JLabel((i+1) + ". ");
                pane.add(finalScores[i]);
            }
        }
    }
    
    /**
     * Helper method that creates the file in which the scores are stored
     * pre: none
     * post: file created, if no file is pre-existing
     */
    private void createFile() {
        //creates a new file
        File highscores = new File("scores.txt");
        
        //if the file exists, a new file is not created
        //if not, a new file is created
        if (highscores.exists()) {
//            System.out.println("File already exists.");
        } else {
            try {
                highscores.createNewFile();
//                System.out.println("New file created.");
            } catch (IOException e) {
                System.out.println("File could not be created.");
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }
    
    /**
     * Helper method that sorts the scores via insertion sort of an array list
     * pre: none
     * post: scores are sorted from fastest to slowest completion time
     */
    private void sortScores() {
        //Unsorted:
//        System.out.println("Unsorted:");
//        for (int i = 0; i < LENGTH; i++) {
//            System.out.println(scores[i]);
//        }
        
        //insertion sort executed with a comparable array list of scores
        Comparable temp;
        int previousIndex;
        
        for (int index = 1; index < scores.size(); index++) {
            
            temp = scores.get(index);
            previousIndex = index - 1;
            
            while ((scores.get(previousIndex).compareTo(temp) > 0) && (previousIndex > 0)) {
                scores.set(previousIndex + 1, scores.get(previousIndex));
                previousIndex -= 1; //decrease index to compare current
            } //item with next previous item
            
            if (scores.get(previousIndex).compareTo(temp) > 0) {
                /* shift item in first element up into next element */
                scores.set(previousIndex + 1, scores.get(previousIndex));
                /* place current item at index 0 (first element) */
                scores.set(previousIndex, temp);
            } else {
                /* place current item at index ahead of previous item */
                scores.set(previousIndex + 1, temp);
            }
        }
        
        //Sorted:
//        System.out.println("\nSorted:");
//        for (int i = 0; i < LENGTH; i++) {
//            System.out.println(scores[i]);
//        }
    }
    
    /**
     * Helper method that updates the JLabel top 10 list with the updated
     * scores in order from fastest to slowest
     * pre: none
     * post: top 10 list updated
     */
    private void updateScores() {
        for (int i = 0; i < length; i++) {
            //updates high score list based on the scores array list
            if (i < 10) {
                finalScores[i].setText((i + 1) + ". " + scores.get(i));
            }
        }
    }
    
    /**
     * Helper method that deletes the current storage of scores in storage
     * such that it can copy the new set of data without overlapping
     * pre: none
     * post: previous high scores deleted
     */
    private void deleteCurrentScores() {
        File highscores = new File("scores.txt");
        
        //deletes file, if it exists
        if (highscores.exists()) {
            highscores.delete();
        }
        
        //creates a new file after deletion, if it existed prior
        createFile();
    }
    
    /**
     * Helper method that adds the memory storage of high scores to the
     * hardware storage of the high scores
     * pre: none
     * post: scores saved into storage
     */
    private void saveScores() {
        File highscores = new File("scores.txt");
        FileWriter outStream;
        BufferedWriter writeFile;
        
        try {
            //creates an output stream and a buffered writer to save scores
            outStream = new FileWriter(highscores, true);
            writeFile = new BufferedWriter(outStream);
            //if there are items to save, this section is executed
            if (scores.size() > 0) {
                for (int i = 0; i < length; i++) {
                    //for a maximum of 10 items, the scores are written into storage
                    if (i < 10) {
                        writeFile.write(Integer.toString((Integer)scores.get(i)));
                        writeFile.newLine();
                    }
                }
            }
            //closes outStream and writeFile
            writeFile.close();
            outStream.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
