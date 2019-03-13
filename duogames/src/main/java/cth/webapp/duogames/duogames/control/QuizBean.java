/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cth.webapp.duogames.duogames.control;

import cth.webapp.duogames.duogames.database.dao.GameDAO;
import cth.webapp.duogames.duogames.database.entity.GameSession;
import cth.webapp.duogames.duogames.model.IQuestion;
import cth.webapp.duogames.duogames.model.listening.WhatDidYouSayQuiz;
import cth.webapp.duogames.duogames.model.quiz.Quiz;
import cth.webapp.duogames.duogames.utils.ScoreCalculator;
import cth.webapp.duogames.duogames.utils.TimeFormatter;
import cth.webapp.duogames.duogames.view.QuizData;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bootsfaces.utils.FacesMessages;

/**
 *
 * @author latiif
 */
@Named(value="quiz")
@SessionScoped
public class QuizBean extends GameBean implements Serializable {
    
    @Inject
    private UserBean userBean;
    
    @Inject
    private ScoreBean scorebean;
    
    @Inject
    private QuizData quizData;
    
    private List<IQuestion> quiz;
    
    @Getter
    private final String type = "quiz";
    
    
    
    private Timestamp startTime;
    private Timestamp endTime;
    

    public List<IQuestion> getQuizInformation(UserBean ub) {
        if (quiz == null) {
            quiz = startGame();
        }
        return quiz;
    }

    @Override
    public List<IQuestion> startGame() {
        Map<String, List<String>> dict = userBean.getApi().getDictionaryOfKnownWords("en", userBean.getApi().getCurrentLanguage());
        quizData.setCurrQuestion(0);
        quizData.setTotalQuestions(10);
        quizData.setNrCorrect(0);
        startTime = new Timestamp(System.currentTimeMillis());
        return new Quiz(dict, 10, 3).generateQuestions();
    }
    
    @Override
    public void resetGame() {
        quiz = null;
        redirect("/duogames/play.xhtml");
    }
    
    public void validate(){
        if (quiz.get(quizData.getCurrQuestion()).check(quizData.getAnswer()))
        {
            FacesMessages.info("Correct!");
            int x =  quizData.getNrCorrect() +1;
            quizData.setNrCorrect(x);
            int y = quizData.getCurrQuestion() + 1;
            quizData.setCurrQuestion(y);
            if(quizData.getCurrQuestion() == 10){
                endGame();
            }
        }
        else{
            FacesMessages.error("Wrong");
            
            int x =  quizData.getCurrQuestion() +1;
            quizData.setCurrQuestion(x);
            if(quizData.getCurrQuestion() == 10){
                endGame();
            } 
        }
    }

    @Override
    public void endGame() {
        endTime = new Timestamp(System.currentTimeMillis());
        long diff = (endTime.getTime() - startTime.getTime());
        long seconds = diff / 1000L;
        quizData.setTime(TimeFormatter.format(seconds));
        quizData.setScore(ScoreCalculator.calculateScore(quizData.getNrCorrect(), diff));
        scorebean.setGamebean(this);
        addToDatabase(quizData.getScore(), seconds, type);
        redirect("/duogames/score.xhtml");
    }

}
