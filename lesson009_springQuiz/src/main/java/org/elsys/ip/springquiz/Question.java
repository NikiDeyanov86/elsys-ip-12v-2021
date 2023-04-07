package org.elsys.ip.springquiz;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String question;
    private final List<Answer> answers;

    private Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String question;
        private List<Answer> answers = new ArrayList<>();

        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder addAnswer(String answer) {
            answers.add(new Answer(answer, false));
            return this;
        }

        public Builder addCorrectAnswer(String answer) {
            answers.add(new Answer(answer, true));
            return this;
        }

        public Question build() {
            // TODO: Check that has only one correct answer.
            return new Question(question, answers);
        }
    }

    public static class Answer {
        private final String answer;
        private final boolean correct;

        private Answer(String answer, boolean correct) {
            this.answer = answer;
            this.correct = correct;
        }

        public String getAnswer() {
            return answer;
        }

        public boolean isCorrect() {
            return correct;
        }
    }
}
