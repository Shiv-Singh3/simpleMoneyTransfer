package simpleMoneyTransfer.manager.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpleMoneyTransfer.constants.Errors;
import simpleMoneyTransfer.exceptions.SimpleMoneyTransferApplicationException;
import simpleMoneyTransfer.manager.spi.TransferWebServiceManager;
import simpleMoneyTransfer.webServices.dto.AccountDTO;
import simpleMoneyTransfer.webServices.dto.TransferDTO;

public class TransferWebServiceManagerImpl implements TransferWebServiceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferWebServiceManagerImpl.class);

    @Inject
    private AccountWebServiceManagerImpl accountWebServiceManagerImpl;

    @Inject
    private MoneyTransferImpl moneyTransferImpl;

    @Override
    public void transfer(TransferDTO transferDTO) {
        Integer sourceAccountNum = transferDTO.getSourceAccountNumber();
        Integer destAccountNum = transferDTO.getDestinationAccountNumber();
        Double amount = transferDTO.getAmount();

        AccountDTO sourceAccount = accountWebServiceManagerImpl.getAccount(sourceAccountNum);
        LOGGER.info("Source Account fetched for account number : {}", sourceAccount.getAccountNumber());
        AccountDTO destAccount = accountWebServiceManagerImpl.getAccount(destAccountNum);
        LOGGER.info("Destination Account fetched for account number : {}", destAccount.getAccountNumber());

        if (isValidTransaction(sourceAccount, destAccount, amount)) {
            sourceAccount = moneyTransferImpl.withdraw(sourceAccount, amount);
            destAccount = moneyTransferImpl.credit(destAccount, amount);
            accountWebServiceManagerImpl.updateAccount(sourceAccount);
            accountWebServiceManagerImpl.updateAccount(destAccount);
        } else {
            LOGGER.error("Unable to initiate transfer, either low balance or currency mismatch. " +
                    "Source Account Number : {}, Source Account Currency : {}, " +
                    "Dest Account Number : {}, Dest Account Currency, Source Account Balance : {}, Amount : {}",
                    sourceAccount.getAccountNumber(), sourceAccount.getCurrency(),
                    destAccount.getAccountNumber(), destAccount.getCurrency(),
                    sourceAccount.getBalance(), amount);
            throw new SimpleMoneyTransferApplicationException(Errors.INVALID_TRANSFER_REQUEST);
        }
    }

    public boolean isValidTransaction(AccountDTO sourceAccountDTO, AccountDTO destAccountDTO, Double amount) {
        return StringUtils.equals(sourceAccountDTO.getCurrency().getCurrencyCode(),
                destAccountDTO.getCurrency().getCurrencyCode()) && sourceAccountDTO.getBalance() >= amount;
    }
}
