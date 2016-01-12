/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise_10;

import com.magto.models.Word;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author MimiKuchiki
 */
public class Exercise_10 {

    public static void main(String args[]) {
        //D:\Our_Files\Eric\Programming_Project\Java\Project_1\Sample Text
        //Sample token directory
        //D:\Our_Files\Eric\Programming_Project\Java\Project_1\TokenFolder\tokens.txt
        //Sample count vector directory
        //D:\Our_Files\Eric\Programming_Project\Java\Project_1\CountVectorFolder\cfv.txt

        String dataDirectory = "";
        String tokenDirectory = "";
        String countVectorDirectory = "";
        String line;
        String filePath;
        String delimString = "";
        String token;
        String query;
        int[][] matrix;
        ArrayList<String> wordBank = new ArrayList();
        ArrayList<Word> listOfWords = new ArrayList();
        ArrayList<ArrayList<Word>> w = new ArrayList();
        ArrayList<String> search = new ArrayList();
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter path: ");
        dataDirectory = scan.nextLine();
        System.out.print("Enter delimeter string: ");
        delimString = scan.nextLine();
        System.out.println("Enter path of textfile for the tokens: ");
        tokenDirectory = scan.nextLine();
        System.out.println("Enter path of textfile for the count vector: ");
        countVectorDirectory = scan.nextLine();

        try {
            File file = new File(dataDirectory);
            File[] listOfFiles = file.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                filePath = dataDirectory + "\\" + listOfFiles[i].getName();
                File f = new File(filePath);
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                System.out.println("Accessing file: " + filePath);

                while ((line = br.readLine()) != null) {
                    line = line.toLowerCase();
                    StringTokenizer st = new StringTokenizer(line, delimString);
                    while (st.hasMoreTokens()) {
                        token = st.nextToken();
                        if (wordBank.isEmpty()) {
                            wordBank.add(token);
                        } else {
                            if (!checkWordBank(token, wordBank)) {
                                wordBank.add(token);
                            }
                        }
                        if (listOfWords.isEmpty()) {
                            Word word = new Word(token);
                            word.append();
                            listOfWords.add(word);
                        } else {
                            if (!checkWordList(token, listOfWords)) {
                                Word word = new Word(token);
                                word.append();
                                listOfWords.add(word);
                            }
                        }
                    }
                }
                w.add(listOfWords);
                listOfWords = new ArrayList<>();
            }

            wordBank = sortWordBank(wordBank);
            boolean flag;
            matrix = new int[listOfFiles.length][wordBank.size()];

            for (int i = 0; i < w.size(); i++) {
                //System.out.println("---------------");
                int c = 0;
                for (int a = 0; a < wordBank.size(); a++) {
                    flag = false;
                    for (int b = 0; b < w.get(i).size(); b++) {
                        if (wordBank.get(a).equalsIgnoreCase(w.get(i).get(b).getWordName())) {
                            matrix[i][c++] = w.get(i).get(b).getWordCount();
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        matrix[i][c++] = 0;
                    }
                }
            }

            System.out.println("Writing tokens at file: " + tokenDirectory);
            flag = writeToken(tokenDirectory, wordBank);
            if (flag) {
                System.out.println("Writing successful.");
            } else {
                System.out.println("Writing not successful.");
            }

            System.out.println("Writing count vector at file: " + countVectorDirectory);
            flag = writeVectorCount(matrix, countVectorDirectory, listOfFiles, wordBank,dataDirectory);
            if (flag) {
                System.out.println("Writing successful.");
            } else {
                System.out.println("Wrinting not successful.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean writeToken(String tokenDirectory, ArrayList<String> wordBank) {
        try {
            File f = new File(tokenDirectory);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            
            fw.flush();
            for (int i = 0; i < wordBank.size(); i++) {
                bw.write("" + wordBank.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(1);
        }
        return true;
    }

    public static boolean writeVectorCount(int[][] matrix, String vectorCountDirectory, File[] listOfFiles, ArrayList<String> wordBank,String dataDirectory) {
        int row = 0;
        try {
            File f = new File(vectorCountDirectory);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            
            
          
            for (int i = 0; i < listOfFiles.length; i++) {
                bw.write(dataDirectory + "\\" + listOfFiles[i].getName()+" ");
                for (int a = 0; a < wordBank.size(); a++) {
                    bw.write(matrix[row][a] + " ");
                }
                bw.newLine();
                row++;
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(1);
        }
        return true;
    }

    public static ArrayList<String> getSearch(String query) {
        ArrayList<String> search = new ArrayList();
        StringTokenizer token = new StringTokenizer(query);
        String word;
        while (token.hasMoreTokens()) {
            word = token.nextToken();
            if (search.isEmpty()) {
                search.add(word);
            } else if (!checkWordBank(word, search)) {
                search.add(word);
            }
        }
        System.out.println("Done get search.");
        return search;
    }

    public static boolean checkWordBank(String word, ArrayList<String> w) {
        boolean flag = false;
        for (int i = 0; i < w.size(); i++) {
            if (word.contentEquals(w.get(i))) {
                flag = true;
            }
        }
        return flag;
    }

    public static ArrayList<String> sortWordBank(ArrayList<String> wordBank) {
        boolean flag = true;
        Collections.sort(wordBank, ALPHABETICAL_ORDER);
        return wordBank;
    }

    public static boolean checkWordList(String word, ArrayList<Word> w) {
        boolean flag = false;
        for (int i = 0; i < w.size(); i++) {
            if (word.contentEquals(w.get(i).getWordName())) {
                flag = true;
                w.get(i).append();
                break;
            }
        }
        return flag;
    }
    
    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

}
