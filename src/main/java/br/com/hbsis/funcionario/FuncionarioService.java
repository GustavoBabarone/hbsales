package br.com.hbsis.funcionario;

import br.com.hbsis.hbemployee.HBEmployeeDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Service
public class FuncionarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);
    private final IFuncionarioRepository iFuncionarioRepository;

    /** DEFINIR O 'RestTemplate' */
    private RestTemplate restTemplate;

    @Autowired /** CONSTRUTOR */
    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository, RestTemplate restTemplate) {
        this.iFuncionarioRepository = iFuncionarioRepository;
        this.restTemplate = restTemplate;
    }

    /** MÉTODOS DE CRUD */
    public FuncionarioDTO salvar(FuncionarioDTO funcionarioDTO){

        this.validarCamposTexto(funcionarioDTO);

        LOGGER.info("Salvando funcionário");
        LOGGER.debug("Funcionario: {}", funcionarioDTO);

        Funcionario funcionario = new Funcionario(  funcionarioDTO.getNome(),
                                                    funcionarioDTO.getEmail(),
                                                    this.validarFuncionarioNaAPI(funcionarioDTO).getEmployeeUuid());

        funcionario = this.iFuncionarioRepository.save(funcionario);

        LOGGER.info("Salvando novo funcionário... sucesso!");

        return FuncionarioDTO.of(funcionario);
    }

    public void validarCamposTexto(FuncionarioDTO funcionarioDTO){

        LOGGER.info("Validando informações do funcionário...");

        if(funcionarioDTO == null){
            throw new IllegalArgumentException("FuncionarioDTo não deve ser nulo");
        }

        if(StringUtils.isEmpty(funcionarioDTO.getNome())){
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(funcionarioDTO.getEmail())){
            throw new IllegalArgumentException("Email não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(funcionarioDTO.getUuid())){
            throw new IllegalArgumentException("Uuid não deve ser nulo/vazio");
        }

        if(funcionarioDTO.getNome().length() > 50){
            throw new IllegalArgumentException("Nome deve conter 50 caracteres");
        }

        if(funcionarioDTO.getEmail().length() > 50){
            throw new IllegalArgumentException("Email deve conter 50 caracteres");
        }

        if(funcionarioDTO.getUuid().length() > 36){
            throw new IllegalArgumentException("Uuid deve conter 36 caracteres");
        }

    }

    public FuncionarioDTO findById(Long id){

        Optional<Funcionario> funcionarioOptional = this.iFuncionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {

            Funcionario funcionario = funcionarioOptional.get();
            FuncionarioDTO funcionarioDTO = FuncionarioDTO.of(funcionario);
            return funcionarioDTO;
        }
        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    /** FORMATAÇÕES GERAL */
    public Funcionario converterObjeto(FuncionarioDTO funcionarioDTO){

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioDTO.getId());
        return funcionario;
    }

    /** API - ATIVIDADE 13 */
    public HBEmployeeDTO validarFuncionarioNaAPI(FuncionarioDTO funcionarioDTO){

        LOGGER.info("Recebendo validação de funcionário na API...");

        /* CHAVE (KEY) DE ACESSO À API */
        String apiKey = "f59ff696-1b67-11ea-978f-2e728ce88125";

        try{

            /* INSTANCIAR OBJ 'Header' PARA CONTRUÇÃO DA URL */
            HttpHeaders headers = new HttpHeaders();

            /* DEFINIR O TIPO DA APLICAÇÃO TESTE */
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            /* ADICIONANDO 'API-KEY' NO 'HttpHeader' */
            headers.add("Authorization", apiKey);

            HttpEntity<FuncionarioDTO> funcionarioHttpEntity = new HttpEntity<>(funcionarioDTO, headers);

            LOGGER.info("Realizando retorno da validação na API...");

            /* RETORNO */ /* ENTIDADE DE SOLICITAÇÃO -> URL DISPONÍVEL NO 'Swagger UI' */
            return this.restTemplate.exchange(
                    "http://10.2.54.25:9999/api/employees/",
                    HttpMethod.POST,
                    funcionarioHttpEntity,
                    HBEmployeeDTO.class).getBody();

        }catch(Exception erroAPI){
            erroAPI.printStackTrace();
        }
        throw new IllegalArgumentException("Falha ao validar funcionário na API...");
    }


}
