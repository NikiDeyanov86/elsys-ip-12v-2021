package org.elsys.ip.springwebcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class CalculatorController {
    @Autowired
    private CalculatorCore calculatorCore;

    @GetMapping("/calculator")
    public String form(Model model) {
        model.addAttribute("input", new CalculatorInput());
        model.addAttribute("result", null);
        return "form";
    }

    @PostMapping("/calculator")
    public String result(@ModelAttribute CalculatorInput input, Model model) {
        String[] lineSplit = input.getCommand().split(" ");
        String result = calculatorCore.execute(
                lineSplit[0],
                Arrays.stream(lineSplit).skip(1).collect(Collectors.toList())
        );

        model.addAttribute("input", input);
        model.addAttribute("result", result);
        return "form";
    }

}
