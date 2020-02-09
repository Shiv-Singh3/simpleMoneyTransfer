package simpleMoneyTransfer.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import simpleMoneyTransfer.webServices.dto.AccountDTO;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CreateAccountJsonParserTest {

    @InjectMocks
    private CreateAccountJsonParser createAccountJsonParser;

    @Test
    public void testParseAccountJsonSuccess() {

        AccountDTO accountDTO = createAccountJsonParser.parseAccountJson(getValidAccountString());
        assertEquals((Integer)1001, accountDTO.getAccountNumber());
    }

    @Test
    public void testParseAccountJsonNewAccountSuccess() {

        AccountDTO accountDTO = createAccountJsonParser.parseAccountJson(getNewAccountString());
        assertEquals((Integer) 0, accountDTO.getAccountNumber());
    }

    String getValidAccountString() {
        return "{\n" +
                "\t\"name\": \"shiv\",\n" +
                "\t\"accountNumber\": 1001,\n" +
                "\t\"balance\": 100.00,\n" +
                "\t\"emailId\": \"shivendra.singh3333@gmail.com\"\n" +
                "}";
    }

    String getNewAccountString() {
        return "{\n" +
                "\t\"name\": \"shiv\",\n" +
                "\t\"emailId\": \"shivendra.singh3333@gmail.com\"\n" +
                "}";
    }
}