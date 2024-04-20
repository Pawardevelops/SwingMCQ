import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class OnlineTest extends JFrame implements ActionListener {
    JLabel questionLabel;
    JRadioButton options[] = new JRadioButton[4];
    JButton nextButton, bookmarkButton, prevButton, resultButton;
    ButtonGroup buttonGroup;
    int count = 0, current = 0, bookmarkCount = 1, totalQuestions=0;
    int bookmarks[] = new int[10];
    String username;
    String[] questions;
    String[][] optionsArray;
    String[] answers;

    public OnlineTest(String username) {
        this.setTitle("Test for "+username);
        this.username = username;

        questionLabel = new JLabel();
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            buttonGroup.add(options[i]);
        }
        nextButton = new JButton("Next");
        bookmarkButton = new JButton("Bookmark");
        prevButton = new JButton("Previous");
        resultButton = new JButton("Result");

        nextButton.addActionListener(this);
        bookmarkButton.addActionListener(this);
        prevButton.addActionListener(this);
        resultButton.addActionListener(this);

        add(questionLabel);
        for (int i = 0; i < 4; i++) {
            add(options[i]);
        }
        add(nextButton);
        add(prevButton);
        add(bookmarkButton);
        add(resultButton);

        setLayout(null);

        questionLabel.setBounds(30, 40, 450, 20);
        for (int i = 0; i < 4; i++) {
            options[i].setBounds(50, 100 + i * 30, 200, 20);
        }
        nextButton.setBounds(100, 280, 100, 30);
        prevButton.setBounds(210, 280, 100, 30);
        bookmarkButton.setBounds(320, 280, 100, 30);
        resultButton.setBounds(480, 20, 100, 30);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        loadQuestionsFromFile();
        setQuestions();
    }

    void loadQuestionsFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("questions.txt"));
            int count = Integer.parseInt(br.readLine());
            totalQuestions=count;
            questions = new String[count];
            optionsArray = new String[count][4];
            answers = new String[count]; // Changed to string array to store answer strings
            for (int i = 0; i < count; i++) {
                questions[i] = br.readLine();
                for (int j = 0; j < 4; j++) {
                    optionsArray[i][j] = br.readLine();
                }
                answers[i] = br.readLine(); // Store the answer string directly
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (checkAnswer()) {
                count++;
            }
            current++;
            setQuestions();
            if (current == totalQuestions-1) {
                nextButton.setEnabled(false);
                bookmarkButton.setEnabled(false);
                resultButton.setVisible(true);
            }
        } else if (e.getSource() == bookmarkButton) {
            JButton bookmarkBtn = new JButton("Bookmark " + bookmarkCount);
            bookmarkBtn.setBounds(480, 20 + 30 * bookmarkCount, 100, 30);
            bookmarkBtn.addActionListener(this);
            add(bookmarkBtn);
            bookmarks[bookmarkCount] = current;
            bookmarkCount++;
            current++;
            setQuestions();
            if (current == 9) {
                bookmarkButton.setEnabled(false);
                resultButton.setVisible(true);
            }
            setVisible(false);
            setVisible(true);
        } else if (e.getSource() == prevButton) {
            if (current > 0) {
                current--;
                setQuestions();
            }
        } else {
            for (int i = 0, y = 1; i < bookmarkCount; i++, y++) {
                if (e.getActionCommand().equals("Bookmark " + y)) {
                    if (checkAnswer())
                        count++;
                    int temp = current;
                    current = bookmarks[y];
                    setQuestions();
                    ((JButton) e.getSource()).setEnabled(false);
                    current = temp;
                }
            }

            if (e.getSource() == resultButton) {
                if (checkAnswer())
                    count++;
                JOptionPane.showMessageDialog(this, "Correct answers: " + count + "\nThank you for taking the quiz!");
                System.exit(0);
            }
        }
    }

    void setQuestions() {
        options[0].setSelected(true);
        
        questionLabel.setText(questions[current]); // Set the current question
        System.out.println("this is question : "+questions[current]);
        
        // Set the options for the current question
        for (int i = 0; i < 4; i++) {
            options[i].setText(optionsArray[current][i]);
        }
    }

    boolean checkAnswer() {
        // Get the index of the selected option
        int selectedOptionIndex = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selectedOptionIndex = i+1;
                break;
            }
        }
        System.out.println("selected index : "+selectedOptionIndex+" and the ans is "+answers[current]);
        
        // Compare the selected option's text with the answer string
        return selectedOptionIndex==Integer.parseInt(answers[current]);
    }
       
}
