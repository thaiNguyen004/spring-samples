package com.booting.validation;

import org.passay.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PasswordRuleValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_COMPLEX_RULES = 2;
    private static final int MAX_REPETITIVE_CHARS = 3;
    private static final int MIN_SPECIAL_CASE_CHARS = 2;
    private static final int MIN_UPPER_CASE_CHARS = 1;
    private static final int MIN_LOWER_CASE_CHARS = 1;
    private static final int MIN_DIGIT_CASE_CHARS = 1;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // using passay library
        List<Rule> passwordRules = new ArrayList<>();
        passwordRules.add(new LengthRule(8, 30) /*min 8,max 30*/);

        // characteristics rule
        List<CharacterRule> characterRules = new ArrayList<>();
        characterRules.add(new CharacterRule(EnglishCharacterData.Special, MIN_SPECIAL_CASE_CHARS));
        characterRules.add(new CharacterRule(EnglishCharacterData.UpperCase, MIN_UPPER_CASE_CHARS));
        characterRules.add(new CharacterRule(EnglishCharacterData.LowerCase, MIN_LOWER_CASE_CHARS));
        characterRules.add(new CharacterRule(EnglishCharacterData.Digit, MIN_DIGIT_CASE_CHARS));

        CharacterCharacteristicsRule characterCharacteristicsRule =
                new CharacterCharacteristicsRule(MIN_COMPLEX_RULES, characterRules);
        passwordRules.add(characterCharacteristicsRule);
        passwordRules.add(new RepeatCharacterRegexRule(MAX_REPETITIVE_CHARS));

        PasswordValidator passwordValidator = new PasswordValidator(passwordRules);
        PasswordData passwordData = new PasswordData(password);
        RuleResult ruleResult = passwordValidator.validate(passwordData);
        return ruleResult.isValid();
    }
}