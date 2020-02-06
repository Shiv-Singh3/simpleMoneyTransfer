package simpleMoneyTransfer.webServices;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;
import simpleMoneyTransfer.manager.AccountWebServiceManager;
import simpleMoneyTransfer.webServices.dto.AccountDTO;
import simpleMoneyTransfer.webServices.validation.ValidCreateAccountJson;
import simpleMoneyTransfer.webServices.validation.ValidLanguageCode;
import java.util.concurrent.ConcurrentHashMap;

@Path("/account")
@Api(value = "Account Web Service")
public class AccountWebServices {

    private AccountWebServiceManager accountWebServiceManager;

    private ConcurrentHashMap<Integer, AccountDTO> accounts;

    @POST
    @Path("/create")
    @ApiOperation(value = "Creates an Account", response = AccountWebServices.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created an account", response = AccountWebServices.class),
            @ApiResponse(code = 404, message = "Not Found", response = AccountWebServices.class),
            @ApiResponse(code = 500, message = "Error while processing the request",response = AccountWebServices.class)
    })
    public Response createAccount(@ValidCreateAccountJson String inputString,
                  @ApiParam(name = "Accept-Language", value = "The value to be passed as header parameter",
                          required = true, defaultValue = "en-US")
                  @HeaderParam("Accept-Language") @ValidLanguageCode String languageCode) {
        accountWebServiceManager = new AccountWebServiceManager();
        accounts = new ConcurrentHashMap<>();
        AccountDTO accountDTO = accountWebServiceManager.parseAccountJson(inputString);
        accountWebServiceManager.createAccount(accountDTO, accounts);
        return Response.status(201).entity(accounts.toString()).build();
    }
}

//todo check for duplicate account creation
//todo fix annotation validation