/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet();
        lettersToWord = new HashMap();
        sizeToWords = new HashMap();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if(lettersToWord.containsKey(sortLetters(word))){
                lettersToWord.get(sortLetters(word)).add(word);
            } else{
                ArrayList<String> value = new ArrayList<>();
                value.add(word);
                lettersToWord.put(sortLetters(word), value);
            }
            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            } else{
                ArrayList<String> value = new ArrayList<>();
                value.add(word);
                sizeToWords.put(word.length(), value);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !(word.contains(base))){
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (String word:wordList){
            if(sortLetters(word).equals(sortLetters(targetWord))){
                result.add(word);
            }
            
        }

        return result;
    }

    public String sortLetters(String word){
        char temp[] = word.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String lexicon = "abcdefghijklmnopqrstuvwxyz";
        char[] alphabet = lexicon.toCharArray();
        for (char letter:alphabet) {
            String alphaWord = sortLetters(letter + word);
            if(lettersToWord.get(alphaWord) != null){
                for (String anagram:lettersToWord.get(alphaWord)) {
                    if(isGoodWord(anagram,word)){
                        result.add(anagram);
                    }

                }


            }

        }

        return result;
    }

    public String pickGoodStarterWord() {

        String word = sizeToWords.get(wordLength).get(random.nextInt(sizeToWords.get(wordLength).size()));

        while(getAnagramsWithOneMoreLetter(word).size() < MIN_NUM_ANAGRAMS){
            word = sizeToWords.get(wordLength).get(random.nextInt(sizeToWords.get(wordLength).size()));

        }

        if(wordLength + 1 < MAX_WORD_LENGTH){
            wordLength = wordLength + 1;
        }

        return word;
    }
}
