package com.example.ebqnkingbackend.services;

import com.example.ebqnkingbackend.dtos.*;
import com.example.ebqnkingbackend.entities.*;
import com.example.ebqnkingbackend.enums.OperationType;
import com.example.ebqnkingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebqnkingbackend.exceptions.BankAccoutNotFoundException;
import com.example.ebqnkingbackend.exceptions.CustomerNotFoundException;
import com.example.ebqnkingbackend.mappers.BankAccountMapperImpl;
import com.example.ebqnkingbackend.repositories.AccountOperationRepository;
import com.example.ebqnkingbackend.repositories.BankAccountRepository;
import com.example.ebqnkingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository cr;
    private BankAccountRepository bac;
    private AccountOperationRepository aor;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustmerDTO saveCustomer(CustmerDTO c) {
        log.info("Saving new Customer");
        Custmer custmer=dtoMapper.fromCustomerDTO(c);
        Custmer savedCustomer=cr.save(custmer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDrift, Long customerId) throws CustomerNotFoundException {
        Custmer custmer=cr.findById(customerId).orElse(null);
        if(custmer==null){
            throw new CustomerNotFoundException("Customer not Found");
        }
        CurrentAccount currentAccount=new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustmer(custmer);
        currentAccount.setOverDraft(overDrift);
        CurrentAccount saveBankAccount=bac.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(saveBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Custmer custmer=cr.findById(customerId).orElse(null);
        if(custmer==null){
            throw new CustomerNotFoundException("Customer not Found");
        }
        SavingAccount savingAccount=new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustmer(custmer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saveBankAccount=bac.save(savingAccount);
        return dtoMapper.fromSavingAccount(saveBankAccount);
    }

    @Override
    public List<CustmerDTO> listCustomer() {
        List<Custmer> custmers=cr.findAll();
        List<CustmerDTO> custmerDTOS=custmers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        /*List<CustmerDTO> custmerDTOS=new ArrayList<>();
        for(Custmer custmer:custmerDTOS){
            CustmerDTO custmerDTO=dtoMapper.fromCustomer(custmer);
            custmerDTOS.add(custmerDTO);
        }*/
        return custmerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccoutNotFoundException {
        BankAccount bankAccount=bac.findById(accountId)
                .orElseThrow(()->new BankAccoutNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccoutNotFoundException {
            BankAccount bankAccount=bac.findById(accountId)
                    .orElseThrow(()->new BankAccoutNotFoundException("BankAccount not found"));

            if(bankAccount.getBalance()<amount){
                throw new BalanceNotSufficientException("Balance n'est pas sufficient");
            }
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        aor.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
            bac.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccoutNotFoundException {
        BankAccount bankAccount=bac.findById(accountId)
                .orElseThrow(()->new BankAccoutNotFoundException("BankAccount not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        aor.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bac.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccoutNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer");
        credit(accountIdDestination,amount,"Transfer from :"+accountIdSource);
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts=bac.findAll();
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.stream().map(bankAccount -> {
          if(bankAccount instanceof SavingAccount){
              SavingAccount savingAccount=(SavingAccount) bankAccount;
              return dtoMapper.fromSavingAccount(savingAccount);
          }else{
              CurrentAccount currentAccount=(CurrentAccount) bankAccount;
              return  dtoMapper.fromCurrentBankAccount(currentAccount);
          }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustmerDTO getCustomer(Long id) throws CustomerNotFoundException {
       Custmer custmer= cr.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(custmer);
    }
    @Override
    public CustmerDTO updateCustomer(CustmerDTO custmerDTO){
        log.info("Update new Customer");
        Custmer custmer=dtoMapper.fromCustomerDTO(custmerDTO);
        Custmer savedCustomer=cr.save(custmer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        cr.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations=aor.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccoutNotFoundException {
        BankAccount bankAccount=bac.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccoutNotFoundException("Acount not found");
        Page<AccountOperation> accountOperations=aor.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountHistoryDTOList(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPage(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustmerDTO> searchCustomers(String keyword) {
        List<Custmer> custmers=cr.searchCustmer(keyword);
        List<CustmerDTO> custmerDTOList=custmers.stream().map(cus->dtoMapper.fromCustomer(cus)).collect(Collectors.toList());
        return custmerDTOList;
    }
}
