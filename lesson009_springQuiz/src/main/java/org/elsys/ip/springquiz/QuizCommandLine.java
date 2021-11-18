package org.elsys.ip.springquiz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class QuizCommandLine implements CommandLineRunner {
    private final QuestionBank bank;
    private final QuestionStringBuilder questionStringBuilder;

    public QuizCommandLine(QuestionBank bank, QuestionStringBuilder questionStringBuilder) {
        this.bank = bank;
        this.questionStringBuilder = questionStringBuilder;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        do {
            Question question = bank.nextQuestion();
            if (question == null) {
                System.out.println("No more questions. Congratulations.");
                break;
            }

            String questionString = questionStringBuilder.toString(question);
            System.out.print(questionString);

            int answerIndex = -1;

            do {
                String userInput = scanner.nextLine();
                try {
                    answerIndex = questionStringBuilder.getAnswerIndex(userInput);

                    if (answerIndex >= question.getAnswers().size()) {
                        System.out.println("The question has only " + question.getAnswers().size() + " answers.");
                        answerIndex = -1;
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
            } while (answerIndex == -1);

            Question.Answer answer = question.getAnswers().get(answerIndex);
            if (!answer.isCorrect()) {
                System.out.println("Answer is not correct.");
                break;
            }

        } while(true);
    }
}
