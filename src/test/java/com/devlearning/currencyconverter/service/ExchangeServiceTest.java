package com.devlearning.currencyconverter.service;

import com.devlearning.currencyconverter.dto.ExchangeRateResponse;
import com.devlearning.currencyconverter.model.ConversionHistory;
import com.devlearning.currencyconverter.model.User;
import com.devlearning.currencyconverter.repository.ConversionHistoryRepository;
import com.devlearning.currencyconverter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@ExtendWith(MockitoExtension.class) // Habilita o uso de @Mock
class ExchangeServiceTest {

    // 1. Criar os "Dublés" (Mocks) das dependências
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConversionHistoryRepository historyRepository;

    @Mock
    private UserRepository userRepository;

    // 2. Injetar os dublés dentro do nosso Service verdadeiro
    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    void deveConverterMoedaComSucesso() {
        // --- CENÁRIO (Given) ---
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal fakeRate = new BigDecimal("0.85");
        
        // Simular a API Externa (ExchangeRateResponse é o record/DTO)
        ExchangeRateResponse fakeApiResponse = new ExchangeRateResponse("success", fakeRate,null);
        
        // Simular o comportamento do RestTemplate (quando chamar a URL, retorna o JSON falso)
        // Nota: Usamos anyString() porque a URL tem a chave de API e é difícil prever a string exata no teste
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class)))
                .thenReturn(fakeApiResponse);

        // Simular o Usuário Logado
        User fakeUser = new User();
        fakeUser.setUsername("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(fakeUser));

        // Simular o salvamento no banco (retorna o próprio objeto que tentou salvar)
        when(historyRepository.save(any(ConversionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // --- MÁGICA DO SECURITY CONTEXT (Mock Estático) ---
        // Como o SecurityContextHolder é estático, precisamos deste bloco try-with-resources
        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            
            // Criar a estrutura falsa de autenticação
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);
            
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("admin");

            // --- AÇÃO (When) ---
            ConversionHistory resultado = exchangeService.convertCurrency(from, to, amount);

            // --- VERIFICAÇÃO (Then) ---
            assertNotNull(resultado);
            assertEquals(new BigDecimal("85.0000"), resultado.getConvertedAmount()); // 100 * 0.85
            assertEquals(fakeUser, resultado.getUser());
            
            // Verifica se o método save foi chamado 1 vez
            verify(historyRepository).save(any(ConversionHistory.class));
        }
    }
    @Test
    void deveLancarErroQuandoMoedaForInvalida() {
        // --- CENÁRIO (Given) ---
        String from = "ZZZ"; // Moeda inventada
        String to = "EUR";
        BigDecimal amount = new BigDecimal("100.00");

        // Simular que a API externa responde com erro 404 (Not Found)
        // Usamos any() porque a URL não importa, o que importa é o erro.
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));

        // --- AÇÃO E VERIFICAÇÃO (When & Then) ---
        // Aqui não usamos assertEquals. Usamos assertThrows para dizer:
        // "Eu espero que este método REBENTE com um IllegalArgumentException"
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            exchangeService.convertCurrency(from, to, amount);
        });

        // Opcional: Verificar se a mensagem de erro é a que definimos
        assertTrue(excecao.getMessage().contains("Moeda inválida"));
    }

    @Test
    void deveFalharSeUsuarioNaoForEncontradoNoBanco() {
        // --- CENÁRIO ---
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = new BigDecimal("100");
        
        // A API externa responde bem (não é aqui que queremos o erro)
        ExchangeRateResponse fakeResponse = new ExchangeRateResponse("success", new BigDecimal("0.85"), null);
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(fakeResponse);

        // O Mock do Security Context diz que é o "admin"
        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);
            
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("admin");

            // --- O ERRO PROVOCADO ---
            // Quando o service procurar "admin" no banco, retorna VAZIO (Optional.empty)
            when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

            // --- VERIFICAÇÃO ---
            RuntimeException erro = assertThrows(RuntimeException.class, () -> {
                exchangeService.convertCurrency(from, to, amount);
            });

            assertEquals("Usuário não encontrado!", erro.getMessage());
        }
    }
    @Test
    void deveRetornarHistoricoApenasDoUsuarioLogado() {
        // --- CENÁRIO (Given) ---
        // 1. Criar um histórico falso para o teste retornar
        ConversionHistory historico1 = new ConversionHistory(); 
        // (Pode configurar os dados se quiser, mas para este teste basta existir)
        List<ConversionHistory> listaEsperada = List.of(historico1);

        // 2. Simular o Security Context (Usuário "joao")
        try (MockedStatic<SecurityContextHolder> mockedSecurity = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Authentication authentication = Mockito.mock(Authentication.class);
            
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("joao");

            // 3. Simular o Repositório
            // Quando o serviço perguntar ao banco pelo histórico do "joao", devolve a lista que criámos
            when(historyRepository.findByUserUsername("joao")).thenReturn(listaEsperada);

            // --- AÇÃO (When) ---
            List<ConversionHistory> resultado = exchangeService.findAllHistoryForCurrentUser();

            // --- VERIFICAÇÃO (Then) ---
            // Verifica se a lista não está vazia
            assertFalse(resultado.isEmpty());
            // Verifica se tem 1 elemento
            assertEquals(1, resultado.size());
            // Verifica se o método do banco foi chamado com o nome "joao" e não outro
            verify(historyRepository).findByUserUsername("joao");
        }
    }
}