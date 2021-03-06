package com.yxy.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yxy.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * BeanValidator
 *
 * @author 余昕宇
 * @date 2018-06-18 10:49
 **/
public class BeanValidator {

    public static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T>Map<String, String> validate(T t, Class... groups) {

        Validator validator = validatorFactory.getValidator();
        Set validateResult = validator.validate(t, groups);
        if (validateResult.isEmpty()) {

            return Collections.emptyMap();

        } else {

            LinkedHashMap errors = Maps.newLinkedHashMap();
            for (Object aValidateResult : validateResult) {

                ConstraintViolation violation = (ConstraintViolation) aValidateResult;
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());

            }

            return errors;

        }

    }

    public static Map<String, String> validateList(Collection<?> collection) {

        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {

            if (!iterator.hasNext()) {

                return Collections.emptyMap();

            }

            Object object = iterator.next();
            errors = validate(object, new Class[0]);

        } while (errors.isEmpty());

        return errors;

    }

    public static Map<String, String> validateObject(Object first, Object... objects) {

        if (objects != null && objects.length > 0) {

            return validateList(Lists.asList(first, objects));

        } else {

            return validate(first);

        }

    }

    public static void check(Object param) throws ParamException {

        Map<String, String> map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new ParamException(map.toString());
        }

    }

}
