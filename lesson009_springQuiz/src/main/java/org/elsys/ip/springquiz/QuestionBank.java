package org.elsys.ip.springquiz;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class QuestionBank {
    private final List<Question> questions = new ArrayList<>() {{
        add(Question.builder().setQuestion("Kolko e 2+2?").addAnswer("1").addAnswer("2").addAnswer("3").addCorrectAnswer("4").build());
        add(Question.builder().setQuestion("Koy e nay-yakia klas?").addAnswer("12 a").addAnswer("12 b").addCorrectAnswer("12 v").addAnswer("12 g").build());
        add(Question.builder().setQuestion("Kolko shte imam po IP?").addAnswer("2").addCorrectAnswer("3").addAnswer("4").addAnswer("5").build());
    }};

    public Question nextQuestion() {
        if (questions.size() == 0) {
            return null;
        }

        Question question = questions.get(0);
        questions.remove(0);
        return question;
    }
}
