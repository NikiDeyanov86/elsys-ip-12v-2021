package org.elsys.ip.springquiz;

import org.springframework.stereotype.Component;

@Component
public class QuestionStringBuilder {
    public String toString(Question question) {
        StringBuilder sb = new StringBuilder();
        sb.append(question.getQuestion());
        sb.append("\n");
        for (int i=0; i<question.getAnswers().size(); ++i) {
            Question.Answer answer = question.getAnswers().get(i);

            sb.append(getAnswerChar(i)).append(": ");
            sb.append(answer.getAnswer()).append("\n");
        }

        return sb.toString();
    }

    public int getAnswerIndex(String userInput) {
        if (userInput.length() != 1) {
            throw new IllegalArgumentException(userInput + " is not 1 in length");
        }

        char inputChar = userInput.toLowerCase().charAt(0);
        int index = inputChar - 'a';
        if (index < 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        return index;
    }

    private char getAnswerChar(int index) {
        return (char)('a' + index);
    }
}
