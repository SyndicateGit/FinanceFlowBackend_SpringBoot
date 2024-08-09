package com.financeflow.financeflow_backend.service.impl;

import com.financeflow.financeflow_backend.dto.TransactionDTO;
import com.financeflow.financeflow_backend.entity.Account;
import com.financeflow.financeflow_backend.entity.Transaction;
import com.financeflow.financeflow_backend.entity.TransactionType;
import com.financeflow.financeflow_backend.entity.User;
import com.financeflow.financeflow_backend.exception.ResourceNotFoundException;
import com.financeflow.financeflow_backend.mapper.TransactionMapper;
import com.financeflow.financeflow_backend.repository.AccountRepository;
import com.financeflow.financeflow_backend.repository.TransactionRepository;
import com.financeflow.financeflow_backend.repository.UserRepository;
import com.financeflow.financeflow_backend.service.TransactionService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private EntityManager entityManager;
    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long accountId) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction transaction = TransactionMapper.mapToTransaction(transactionDTO);

        try{
            Transaction savedTransaction = transactionRepository.save(transaction);
            savedTransaction.applyTransaction(account);
            accountRepository.save(account);
            return TransactionMapper.mapToTransactionDTO(savedTransaction);
        } catch (Exception e){
            throw new ResourceNotFoundException("Transaction not saved");
        }
    }

    @Override
    @Transactional
    public String createTransferTransaction(TransactionDTO transactionDTO, Long from_account_id, Long to_account_id) {
        if(Objects.equals(from_account_id, to_account_id)){
            throw new ResourceNotFoundException("Cannot transfer to the same account");
        }

        Account from_account = accountRepository
                .findById(from_account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Account to_account = accountRepository
                .findById(to_account_id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction to_transaction = TransactionMapper.mapToTransaction(transactionDTO);
        Transaction from_transaction = TransactionMapper.mapToTransaction(transactionDTO);

        entityManager.persist(to_transaction);
        entityManager.persist(from_transaction);
        entityManager.flush();

        to_transaction.setNote("Transfer from account " + from_account_id);
        to_transaction.setDescription(from_account_id + " -> " + to_account_id);
        to_transaction.setCategory("Transfer");
        to_transaction.setLinkedTransactionId(from_transaction.getId());

        from_transaction.setNote("Transfer to account " + to_account_id);
        from_transaction.setDescription(from_account_id + " -> " + to_account_id);
        from_transaction.setCategory("Transfer");
        from_transaction.setLinkedTransactionId(to_transaction.getId());

        try{

            from_account.transfer(to_account, from_transaction.getAmount());

            from_account.addTransaction(from_transaction);
            to_account.addTransaction(to_transaction);

            accountRepository.save(from_account);
            accountRepository.save(to_account);

            return "Transfer saved";
        } catch (Exception e){
            System.out.println(e);
            throw new ResourceNotFoundException("Transaction not saved");
        }
    }

    @Override
    public TransactionDTO findTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return TransactionMapper.mapToTransactionDTO(transaction);
    }

    @Override
    public List<TransactionDTO> findTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        List<Transaction> transactions = account.getTransactions();
        return transactions
                .stream()
                .map(TransactionMapper::mapToTransactionDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Account> accounts = user.getAccounts();
        return accounts.stream()
                .map(Account::getTransactions)
                .flatMap(List::stream)
                .map(TransactionMapper::mapToTransactionDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> findAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(TransactionMapper::mapToTransactionDTO)
                .toList();
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, Long id) {
        if(transactionDTO.getType() == TransactionType.TRANSFER){
            throw new ResourceNotFoundException("Cannot update transfer transaction");
        }

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Account account = accountRepository.findByTransactionsContaining(transaction)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        revertTransaction(id);

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setCategory(transactionDTO.getCategory());
        transaction.setNote(transactionDTO.getNote());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setCurrency(transactionDTO.getCurrency());
        transaction.setRecurring(transactionDTO.isRecurring());
        transaction.setRecurringType(transactionDTO.getRecurringType());

        Transaction savedTransaction = transactionRepository.save(transaction);
        savedTransaction.applyTransaction(account);

        accountRepository.save(account);
        System.out.println(account.getBalance());
        return TransactionMapper.mapToTransactionDTO(savedTransaction);
    }

    @Override
    @Transactional
    public String revertTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getType() == TransactionType.TRANSFER) {
            Long from_account_id = Long.parseLong(transaction.getDescription().split(" -> ")[0]);
            Long to_account_id = Long.parseLong(transaction.getDescription().split(" -> ")[1]);
            Account from_account = accountRepository.findById(from_account_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            Account to_account = accountRepository.findById(to_account_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

            transaction.revertTransfer(from_account, to_account);

            Long linkedTransactionId = transaction.getLinkedTransactionId();

            from_account.removeTransaction(transaction);
            to_account.removeTransaction(transaction);
            from_account.removeTransaction(linkedTransactionId);
            to_account.removeTransaction(linkedTransactionId);

            accountRepository.save(from_account);
            accountRepository.save(to_account);
            return "Transfer reverted";
        }

        // For non-transfer transactions
        Account account = accountRepository.findByTransactionsContaining(transaction)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(transaction.getType() == TransactionType.INCOME){
            account.withdraw(transaction.getAmount());
        } else {
            account.deposit(transaction.getAmount());
        }

        // Remove transaction from account
        account.removeTransaction(transaction.getId());
        accountRepository.save(account);
        return "Transaction deleted";
    }


}
