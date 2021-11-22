package org.elsys.ip.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class QuizController {
    private final QuestionBank bank;
    private final QuestionStringBuilder questionStringBuilder;
    private Question question;
    private boolean gameOver = false;

    public QuizController(QuestionBank bank, QuestionStringBuilder questionStringBuilder) {
        this.bank = bank;
        this.questionStringBuilder = questionStringBuilder;
        question = bank.nextQuestion();
    }


    @GetMapping
    public String getQuestion() {
        if (gameOver) {
            return "Game over.";
        }

        if (question == null) {
            return "No more questions. Congratulations.";
        }

        String questionString = questionStringBuilder.toString(question);
        return questionString;
    }

    @PostMapping(path = "/answer/{answer}")
    public String answer(@PathVariable String answer) {
        int answerIndex = questionStringBuilder.getAnswerIndex(answer);

        if (answerIndex >= question.getAnswers().size()) {
            throw new IllegalArgumentException("The question has only " + question.getAnswers().size() + " answers.");
        }

        Question.Answer answerModel = question.getAnswers().get(answerIndex);

        if (!answerModel.isCorrect()) {
            gameOver = true;
            return "Answer is not correct.";
        }

        question = bank.nextQuestion();

        return getQuestion();
    }
}
