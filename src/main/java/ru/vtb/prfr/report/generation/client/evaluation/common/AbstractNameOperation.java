package ru.vtb.prfr.report.generation.client.evaluation.common;

import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.RoundingMode;

// TODO is a relationship here is not correct. I mean, that FirsNameOperation for example should not extend this class,
//  rather it should use  AbstractNameOperation (the name also should be changed) as a dependency here, as a private field.
public abstract class AbstractNameOperation extends Evaluator<String> {

    protected String[] getNameParts(String value) {
        if (!StringUtils.hasLength(value)) {
            return new String[0];
        }

        return value.trim().split(" ", 3);
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.UNNECESSARY;
    }
}
