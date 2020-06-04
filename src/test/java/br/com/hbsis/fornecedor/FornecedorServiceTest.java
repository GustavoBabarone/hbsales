package br.com.hbsis.fornecedor;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FornecedorServiceTest {

    @Mock
    private IFornecedorRepository iFornecedorRepository;

    @Captor
    private ArgumentCaptor<Fornecedor> argumentCaptor;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    public void salvar() {

        FornecedorDTO fornecedorDTO = new FornecedorDTO(
                "HBSIS SOLUCOES EM TECNOLOGIA DA INFORMACAO LTDA",
                "81875973000176",
                "HBSIS",
                "Rua angelo dias, 220",
                "5547999887766",
                "empresa@hbsis.com.br"
        );

        Fornecedor fornecedorMock = Mockito.mock(Fornecedor.class);

        when(fornecedorMock.getRazaoSocial()).thenReturn(fornecedorDTO.getRazaoSocial());
        when(fornecedorMock.getCnpj()).thenReturn(fornecedorDTO.getCnpj());
        when(fornecedorMock.getNomeFantasia()).thenReturn(fornecedorDTO.getNomeFantasia());
        when(fornecedorMock.getEndereco()).thenReturn(fornecedorDTO.getEndereco());
        when(fornecedorMock.getTelefoneContato()).thenReturn(fornecedorDTO.getTelefoneContato());
        when(fornecedorMock.getEmailContato()).thenReturn(fornecedorDTO.getEmailContato());

        when(this.iFornecedorRepository.save(any())).thenReturn(fornecedorMock);

        this.fornecedorService.salvar(fornecedorDTO);

        verify(this.iFornecedorRepository, times(1)).save(this.argumentCaptor.capture());
        Fornecedor fornecedorCreated = argumentCaptor.getValue();

        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getRazaoSocial()), "Razão social não deve ser nula");
        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getCnpj()), "Cnpj não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getNomeFantasia()), "Nome fantasia não deve ser nula");
        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getEndereco()), "Endereco não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getTelefoneContato()), "Telefone não deve ser nulo");
        assertTrue(StringUtils.isNoneEmpty(fornecedorCreated.getEmailContato()), "Email não deve ser nulo");
    }
} 