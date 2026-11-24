package com.example.parcial.analytics.service;

import com.example.parcial.analytics.dto.AccountCreateRequest;
import com.example.parcial.analytics.dto.AccountDetailResponse;
import com.example.parcial.analytics.dto.AccountResponse;
import com.example.parcial.analytics.dto.AccountUpdateRequest;
import com.example.parcial.analytics.dto.CustomerCreateRequest;
import com.example.parcial.analytics.dto.CustomerDetailResponse;
import com.example.parcial.analytics.dto.CustomerResponse;
import com.example.parcial.analytics.dto.CustomerUpdateRequest;
import com.example.parcial.analytics.dto.MovementRequest;
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
import com.example.parcial.common.ConflictException;
import com.example.parcial.common.InvalidDataException;
import com.example.parcial.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        return toCustomerDetail(findCustomer(username));
    }

    public CustomerDetailResponse createCustomer(CustomerCreateRequest request) {
        if (customerRepository.existsByUsername(request.username())) {
            throw new ConflictException("Ya existe un cliente con username " + request.username());
        }

        Customer customer = Customer.builder()
                .username(request.username())
                .name(request.name())
                .address(request.address())
                .birthdate(request.birthdate())
                .email(request.email())
                .active(request.active())
                .accounts(validateAccounts(request.accounts()))
                .tierAndDetails(Map.of())
                .build();

        return toCustomerDetail(customerRepository.save(customer));
    }

    public CustomerDetailResponse updateCustomer(String username, CustomerUpdateRequest request) {
        Customer customer = findCustomer(username);

        customer.setName(request.name());
        customer.setAddress(request.address());
        customer.setBirthdate(request.birthdate());
        customer.setEmail(request.email());
        customer.setActive(request.active());

        if (request.accounts() != null) {
            customer.setAccounts(validateAccounts(request.accounts()));
        }

        return toCustomerDetail(customerRepository.save(customer));
    }

    public AccountDetailResponse getAccount(Integer accountId) {
        return toAccountDetail(findAccount(accountId), transactionRepository.findByAccountId(accountId));
    }

    public AccountDetailResponse createAccount(AccountCreateRequest request) {
        if (!accountRepository.findByAccountId(request.accountId()).isEmpty()) {
            throw new ConflictException("Ya existe una cuenta con account_id " + request.accountId());
        }

        Account account = Account.builder()
                .accountId(request.accountId())
                .limit(request.limit())
                .products(request.products() == null ? List.of() : request.products())
                .build();

        return toAccountDetail(accountRepository.save(account), List.of());
    }

    public AccountDetailResponse updateAccount(Integer accountId, AccountUpdateRequest request) {
        Account account = findUniqueAccount(accountId);
        account.setLimit(request.limit());

        if (request.products() != null) {
            account.setProducts(request.products());
        }

        Account saved = accountRepository.save(account);
        return toAccountDetail(saved, transactionRepository.findByAccountId(accountId));
    }

    public List<MovementResponse> getMovements(Integer accountId) {
        findAccount(accountId);

        return transactionRepository.findByAccountId(accountId)
                .stream()
                .filter(doc -> doc.getTransactions() != null)
                .flatMap(doc -> doc.getTransactions().stream())
                .sorted(Comparator.comparing(Movement::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toMovementResponse)
                .toList();
    }

    public MovementResponse addMovement(Integer accountId, MovementRequest request) {
        findUniqueAccount(accountId);

        List<TransactionDoc> docs = transactionRepository.findByAccountId(accountId);
        if (docs.size() > 1) {
            throw new ConflictException("Hay mas de un documento de transacciones para account_id " + accountId);
        }

        TransactionDoc doc = docs.isEmpty()
                ? TransactionDoc.builder()
                        .accountId(accountId)
                        .transactionCount(0)
                        .transactions(new ArrayList<>())
                        .build()
                : docs.get(0);

        List<Movement> movements = doc.getTransactions() == null
                ? new ArrayList<>()
                : new ArrayList<>(doc.getTransactions());

        Movement movement = Movement.builder()
                .date(request.date())
                .amount(request.amount())
                .transactionCode(request.transactionCode())
                .symbol(request.symbol())
                .price(request.price())
                .total(request.total())
                .build();

        movements.add(movement);
        doc.setTransactions(movements);
        doc.setTransactionCount(movements.size());
        doc.setBucketStartDate(minDate(movements));
        doc.setBucketEndDate(maxDate(movements));

        transactionRepository.save(doc);
        return toMovementResponse(movement);
    }

    private Customer findCustomer(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + username));
    }

    private Account findAccount(Integer accountId) {
        List<Account> accounts = accountRepository.findByAccountId(accountId);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("Cuenta no encontrada: " + accountId);
        }
        return accounts.get(0);
    }

    private Account findUniqueAccount(Integer accountId) {
        List<Account> accounts = accountRepository.findByAccountId(accountId);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("Cuenta no encontrada: " + accountId);
        }
        if (accounts.size() > 1) {
            throw new ConflictException("account_id duplicado, no se puede modificar: " + accountId);
        }
        return accounts.get(0);
    }

    private List<Integer> validateAccounts(List<Integer> accountIds) {
        if (accountIds == null || accountIds.isEmpty()) {
            return List.of();
        }

        List<Integer> distinctIds = accountIds.stream().distinct().toList();
        Set<Integer> existingIds = accountRepository.findByAccountIdIn(distinctIds)
                .stream()
                .map(Account::getAccountId)
                .collect(Collectors.toSet());

        List<Integer> missing = distinctIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        if (!missing.isEmpty()) {
            throw new InvalidDataException("Cuentas inexistentes: " + missing);
        }

        return distinctIds;
    }

    private CustomerDetailResponse toCustomerDetail(Customer customer) {
        List<Integer> accountIds = customer.getAccounts() == null ? List.of() : customer.getAccounts();

        List<AccountResponse> accounts = accountIds.isEmpty()
                ? List.of()
                : accountRepository.findByAccountIdIn(accountIds)
                        .stream()
                        .sorted(Comparator.comparing(Account::getAccountId,
                                Comparator.nullsLast(Comparator.naturalOrder())))
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

    private AccountDetailResponse toAccountDetail(Account account, List<TransactionDoc> docs) {
        int transactionCount = docs.stream()
                .map(TransactionDoc::getTransactionCount)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        Instant start = docs.stream()
                .map(TransactionDoc::getBucketStartDate)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);

        Instant end = docs.stream()
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

    private Instant minDate(List<Movement> movements) {
        return movements.stream()
                .map(Movement::getDate)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    private Instant maxDate(List<Movement> movements) {
        return movements.stream()
                .map(Movement::getDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
