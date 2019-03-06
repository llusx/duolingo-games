/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cth.webapp.duogames.duogames.model.listening;

import cth.webapp.duogames.duogames.model.IQuestion;

/**
 *
 * @author latiif
 */
public class ListeningQuestion implements IQuestion {
    
    private String wordUrl;
    private String correctAnswer;
    
    
    public ListeningQuestion(String wordUrl, String answer){
        this.wordUrl = wordUrl;
        this.correctAnswer = answer;
    }
    
    public String getUrl(){
        return wordUrl;
    }
    
    @Override
    public boolean check(String answer){
        return answer.toLowerCase().equals(correctAnswer.toLowerCase());
    }
    
}