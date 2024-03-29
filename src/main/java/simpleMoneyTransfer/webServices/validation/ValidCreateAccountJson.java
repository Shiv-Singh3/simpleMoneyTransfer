package simpleMoneyTransfer.webServices.validation;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import simpleMoneyTransfer.constants.CommonConstants;
import simpleMoneyTransfer.utils.CommonUtils;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Currency;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonValidator.class)
public @interface ValidCreateAccountJson {

    String message() default "{ Invalid Language Code }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Slf4j
class JsonValidator implements ConstraintValidator<ValidCreateAccountJson, Object> {

    @Override
    public void initialize(ValidCreateAccountJson validJson) {
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        String accountJson = (String) object;
        try {
            JSONObject jsonObject = new JSONObject(accountJson);

            String name = (String) CommonUtils.getObjectFromJson(jsonObject, CommonConstants.NAME);
            Integer accountNumber = (Integer) CommonUtils.getObjectFromJson(jsonObject, CommonConstants.ACCOUNT_NUMBER);
            Double balance = (Double) CommonUtils.getObjectFromJson(jsonObject, CommonConstants.BALANCE);
            Currency currency = (Currency) CommonUtils.getObjectFromJson(jsonObject, CommonConstants.CURRENCY);

            if (name == null || name.isEmpty()) {
                log.error("Cannot create Account with empty Name");
                return false;
            }

            if ((accountNumber == null || accountNumber == 0) && (balance != null || currency != null)) {
                log.error("Cannot Create A Balance/Currency Account with empty Account Number");
                return false;
            }
        } catch (JSONException e) {
            log.error("Exception occurred while validating Json");
            return false;
        }
        return true;
    }
}

