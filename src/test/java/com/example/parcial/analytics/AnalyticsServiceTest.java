package com.example.parcial.analytics;

import com.example.parcial.analytics.dto.AccountDetailResponse;
import com.example.parcial.analytics.dto.CustomerCreateRequest;
import com.example.parcial.analytics.dto.CustomerDetailResponse;
import com.example.parcial.analytics.dto.CustomerUpdateRequest;
import com.example.parcial.analytics.dto.MovementRequest;
import com.example.parcial.analytics.dto.MovementResponse;
import com.example.parcial.analytics.model.Account;
import com.example.parcial.analytics.model.Customer;
import com.example.parcial.analytics.model.Movement;
import com.example.parcial.analytics.model.Tier;
import com.example.parcial.analytics.model.TransactionDoc;
import com.example.parcial.analytics.repository.AccountRepository;
import com.example.parcial.analytics.repository.CustomerRepository;
import com.example.parcial.analytics.repository.TransactionRepository;
import com.example.parcial.analytics.service.AnalyticsService;
import com.example.parcial.common.ConflictException;
import com.example.parcial.common.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AnalyticsService service;

    @Test
    void getCustomerResuelveCuentasYTiers() {
        Customer customer = Customer.builder()
                .username("fmiller")
                .name("Elizabeth Ray")
                .accounts(List.of(371138, 324287))
                .tierAndDetails(Map.of("uuid-1", Tier.builder().tier("Bronze").active(true).build()))
                .build();

        when(customerRepository.findByUsername("fmiller")).thenReturn(Optional.of(customer));
        when(accountRepository.findByAccountIdIn(List.of(371138, 324287))).thenReturn(List.of(
                Account.builder().accountId(371138).limit(9000).products(List.of("InvestmentStock")).build(),
                Account.builder().accountId(324287).limit(3000).products(List.of("Brokerage")).build()
        ));

        CustomerDetailResponse response = service.getCustomer("fmiller");

        assertEquals("fmiller", response.username());
        assertEquals(2, response.accounts().size());
        assertEquals("Bronze", response.tierAndDetails().get("uuid-1").tier());
    }

    @Test
    void getAccountSumaDocumentosDuplicadosDeTransacciones() {
        Account account = Account.builder()
                .accountId(627788)
                .limit(10000)
                .products(List.of("Brokerage"))
                .build();

        TransactionDoc first = TransactionDoc.builder()
                .accountId(627788)
                .transactionCount(54)
                .bucketStartDate(Instant.parse("1964-06-27T00:00:00Z"))
                .bucketEndDate(Instant.parse("2017-01-02T00:00:00Z"))
                .build();

        TransactionDoc second = TransactionDoc.builder()
                .accountId(627788)
                .transactionCount(22)
                .bucketStartDate(Instant.parse("1965-02-06T00:00:00Z"))
                .bucketEndDate(Instant.parse("2017-01-05T00:00:00Z"))
                .build();

        when(accountRepository.findByAccountId(627788)).thenReturn(List.of(account));
        when(transactionRepository.findByAccountId(627788)).thenReturn(List.of(first, second));

        AccountDetailResponse response = service.getAccount(627788);

        assertEquals(76, response.transactionCount());
        assertEquals(Instant.parse("1964-06-27T00:00:00Z"), response.bucketStartDate());
        assertEquals(Instant.parse("2017-01-05T00:00:00Z"), response.bucketEndDate());
    }

    @Test
    void getMovementsOrdenaPorFechaDescendente() {
        Account account = Account.builder().accountId(371138).build();
        Movement older = Movement.builder().date(Instant.parse("2001-01-01T00:00:00Z")).symbol("adbe").build();
        Movement newer = Movement.builder().date(Instant.parse("2016-01-01T00:00:00Z")).symbol("msft").build();

        TransactionDoc doc = TransactionDoc.builder()
                .accountId(371138)
                .transactions(List.of(older, newer))
                .build();

        when(accountRepository.findByAccountId(371138)).thenReturn(List.of(account));
        when(transactionRepository.findByAccountId(371138)).thenReturn(List.of(doc));

        List<MovementResponse> movements = service.getMovements(371138);

        assertEquals("msft", movements.get(0).symbol());
        assertEquals("adbe", movements.get(1).symbol());
    }

    @Test
    void cuentaInexistenteLanza404() {
        when(accountRepository.findByAccountId(999999)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.getAccount(999999));
    }

    @Test
    void crearClienteValidaCuentasYDevuelveDetalle() {
        Account account = Account.builder().accountId(371138).limit(9000).products(List.of("InvestmentStock")).build();

        when(customerRepository.existsByUsername("nuevo")).thenReturn(false);
        when(accountRepository.findByAccountIdIn(List.of(371138))).thenReturn(List.of(account));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer saved = invocation.getArgument(0);
            saved.setId("c1");
            return saved;
        });

        CustomerDetailResponse response = service.createCustomer(new CustomerCreateRequest(
                "nuevo", "Cliente Nuevo", null, null, "nuevo@mail.com", true, List.of(371138)));

        assertEquals("nuevo", response.username());
        assertEquals(1, response.accounts().size());
    }

    @Test
    void crearClienteConUsernameDuplicadoLanza409() {
        when(customerRepository.existsByUsername("fmiller")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.createCustomer(new CustomerCreateRequest(
                "fmiller", "Otro", null, null, "otro@mail.com", true, List.of())));
    }

    @Test
    void actualizarClienteConservaCuentasSiNoSeEnvian() {
        Customer customer = Customer.builder()
                .username("fmiller")
                .name("Elizabeth Ray")
                .accounts(List.of(371138))
                .build();

        when(customerRepository.findByUsername("fmiller")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.findByAccountIdIn(List.of(371138))).thenReturn(List.of(
                Account.builder().accountId(371138).limit(9000).products(List.of()).build()));

        CustomerDetailResponse response = service.updateCustomer("fmiller", new CustomerUpdateRequest(
                "Elizabeth Ray Actualizada", null, null, "nuevo@mail.com", true, null));

        assertEquals("Elizabeth Ray Actualizada", response.name());
        assertEquals(1, response.accounts().size());
    }

    @Test
    void agregarMovimientoActualizaContadorYFechas() {
        Account account = Account.builder().accountId(371138).limit(9000).products(List.of()).build();
        Movement existing = Movement.builder().date(Instant.parse("2001-01-01T00:00:00Z")).build();
        TransactionDoc doc = TransactionDoc.builder()
                .accountId(371138)
                .transactionCount(1)
                .bucketStartDate(Instant.parse("2001-01-01T00:00:00Z"))
                .bucketEndDate(Instant.parse("2001-01-01T00:00:00Z"))
                .transactions(new ArrayList<>(List.of(existing)))
                .build();

        when(accountRepository.findByAccountId(371138)).thenReturn(List.of(account));
        when(transactionRepository.findByAccountId(371138)).thenReturn(List.of(doc));
        when(transactionRepository.save(any(TransactionDoc.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MovementResponse response = service.addMovement(371138, new MovementRequest(
                Instant.parse("2016-01-20T00:00:00Z"), 8789, "buy", "crm", "70.77", "621997.55"));

        assertEquals("crm", response.symbol());
        assertEquals(2, doc.getTransactionCount());
        assertEquals(Instant.parse("2001-01-01T00:00:00Z"), doc.getBucketStartDate());
        assertEquals(Instant.parse("2016-01-20T00:00:00Z"), doc.getBucketEndDate());
    }

    @Test
    void actualizarCuentaDuplicadaLanza409() {
        when(accountRepository.findByAccountId(627788)).thenReturn(List.of(
                Account.builder().accountId(627788).build(),
                Account.builder().accountId(627788).build()));

        assertThrows(ConflictException.class, () -> service.updateAccount(627788,
                new com.example.parcial.analytics.dto.AccountUpdateRequest(10000, List.of())));
    }
}
