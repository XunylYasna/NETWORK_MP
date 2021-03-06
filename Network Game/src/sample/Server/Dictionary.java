package sample.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.HashSet;

public class Dictionary {
    //private Set<String> wordsSet;
    HashSet<String> wordsSet = new HashSet<String>();

    public Dictionary(){
        Path path = Paths.get("src/sample/server/words.txt");
        byte[] readBytes = new byte[0];
        try {
            readBytes = Files.readAllBytes(path);
            String wordListContents = new String(readBytes, "UTF-8");
            String[] words = wordListContents.split("\n");
            for(int i = 0; i < words.length; i++){
                if(words[i].substring(0,words[i].length() - 1).length() > 2){
                    wordsSet.add(words[i].substring(0,words[i].length() - 1).toLowerCase());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //wordsSet = new HashSet<>();
        //Collections.addAll(wordsSet, words);

        /*instead of saying Collenctions.addAll, I thought using a loop to insert the values
          one by one would fix the problem, pero hindi parin pala
         */


//        uncomment to print out all the words
//        for (String temp : wordsSet) {
//            System.out.println(temp);
//            System.out.println(temp.length());
//        }

        //.contains the word massive, but sadly it prints out false
        System.out.println(wordsSet.contains("dead"));

    }

    public boolean checkWordExists(String word)
    {
        word = word.trim();
        System.out.println(word+"123 " + wordsSet.contains(word.toLowerCase()));
        return wordsSet.contains(word.toLowerCase());
    }
}
