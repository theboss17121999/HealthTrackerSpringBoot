package com.example.HealthTracker.aspect;

import com.example.HealthTracker.CustomAnnotations.AgeAnnotation;
import com.example.HealthTracker.model.Users;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Aspect
@Component
public class AgeValidationAspect {

    @Before("@annotation(testAnnotation)")
    public void validateAge(
            JoinPoint joinPoint,
            AgeAnnotation testAnnotation) {

        int minAge = testAnnotation.minAgeRequired();

        for (Object arg : joinPoint.getArgs()) {

            if (arg instanceof Users user) {

                if (user.getDateOfBirth() == null) {
                    throw new IllegalArgumentException("Date of birth is required.");
                }

                LocalDate birthDate = user.getDateOfBirth()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                int age = Period.between(birthDate, LocalDate.now()).getYears();

                if (age < minAge) {
                    throw new IllegalArgumentException(
                            "User must be at least " + minAge + " years old."
                    );
                }
            }
        }
    }
}
