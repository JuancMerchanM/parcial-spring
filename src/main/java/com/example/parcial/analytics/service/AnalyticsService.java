package com.example.parcial.analytics.service;

import com.example.parcial.analytics.dto.AccountDetailResponse;
import com.example.parcial.analytics.dto.AccountResponse;
import com.example.parcial.analytics.dto.CustomerDetailResponse;
import com.example.parcial.analytics.dto.CustomerResponse;
import com.example.parcial.analytics.dto.MovementResponse;
import com.example.parcial.analytics.dto.TierResponse;
import com.example.parcial.analytics.model.Account;
import com.example.parcial.analytics.model.Customer;
import com.example.parcial.analytics.model.Movement;
import com.example.parcial.analytics.model.Tier;
import com.example.parcial.analytics.model.TransactionDoc;
import com.example.parcial.analytics.repository.AccountRepository;
import com.example.parcial.analytics.repository.CustomerRepository;
import com.example.parcial.analytics.repository.TransactionRepository;
import com.example.parcial.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Resuelve las relaciones manualmente porque el dataset no usa @DBRef:
 *
 * customers.accounts[] -> accounts.account_id -> transactions.account_id
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<CustomerResponse> listCustomers() {
        return customerRepository.findAllByOrderByUsernameAsc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public CustomerDetailResponse getCustomer(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + username));

        List<Integer> accountIds = customer.getAccounts() == null ? List.of() : customer.getAccounts();

        List<AccountResponse> accounts = accountIds.isEmpty()
                ? List.of()
                : accountRepository.findByAccountIdIn(accountIds)
                        .stream()
                        .sorted(Comparator.comparing(Account::getAccountId, Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(this::toAccountResponse)
                        .toList();

        return new CustomerDetailResponse(
                customer.getUsername(),
                customer.getName(),
                customer.getAddress(),
                customer.getBirthdate(),
                customer.getEmail(),
                customer.getActive(),
                accounts,
                toTierMap(customer.getTierAndDetails())
        );
    }

    public AccountDetailResponse getAccount(Integer accountId) {
        Account account = findAccount(accountId);
        List<TransactionDoc> transactionDocs = transactionRepository.findByAccountId(accountId);

        int transactionCount = transactionDocs.stream()
                .map(TransactionDoc::getTransactionCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        Instant start = transactionDocs.stream()
                .map(TransactionDoc::getBucketStartDate)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        Instant end = transactionDocs.stream()
                .map(TransactionDoc::getBucketEndDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

        return new AccountDetailResponse(
                account.getAccountId(),
                account.getLimit(),
                account.getProducts(),
                transactionCount,
                start,
                end
        );
    }

    public List<MovementResponse> getMovements(Integer accountId) {
        findAccount(accountId); // valida que la cuenta exista

        return transactionRepository.findByAccountId(accountId)
                .stream()
                .filter(doc -> doc.getTransactions() != null)
                .flatMap(doc -> doc.getTransactions().stream())
                .sorted(Comparator.comparing(Movement::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toMovementResponse)
                .toList();
    }

    private Account findAccount(Integer accountId) {
        List<Account> accounts = accountRepository.findByAccountId(accountId);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("Cuenta no encontrada: " + accountId);
        }
        // El dataset tiene dos documentos con account_id 627788; se usa el primero.
        return accounts.get(0);
    }

    private CustomerResponse toSummary(Customer customer) {
        int accountsCount = customer.getAccounts() == null ? 0 : customer.getAccounts().size();
        return new CustomerResponse(
                customer.getUsername(),
                customer.getName(),
                customer.getEmail(),
                customer.getActive(),
                accountsCount
        );
    }

    private AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(account.getAccountId(), account.getLimit(), account.getProducts());
    }

    private MovementResponse toMovementResponse(Movement movement) {
        return new MovementResponse(
                movement.getDate(),
                movement.getAmount(),
                movement.getTransactionCode(),
                movement.getSymbol(),
                movement.getPrice(),
                movement.getTotal()
        );
    }

    private Map<String, TierResponse> toTierMap(Map<String, Tier> tiers) {
        if (tiers == null || tiers.isEmpty()) {
            return Map.of();
        }
        return tiers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toTierResponse(entry.getValue()),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private TierResponse toTierResponse(Tier tier) {
        return new TierResponse(tier.getTier(), tier.getId(), tier.getActive(), tier.getBenefits());
    }
}
