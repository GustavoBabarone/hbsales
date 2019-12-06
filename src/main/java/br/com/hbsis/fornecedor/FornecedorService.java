package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classe responsável pelo processamento da regra de negócio
 */
@Service
public class FornecedorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final IFornecedorRepository iFornecedorRepository;

    @Autowired
    public FornecedorService(IFornecedorRepository iFornecedorRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public FornecedorDTO save(FornecedorDTO fornecedorDTO){

        this.validate(fornecedorDTO);

        LOGGER.info("Salvando fornecedor");
        LOGGER.debug("Fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(fornecedorDTO.getRazaoSocial());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomeFantasia(fornecedorDTO.getNomeFantasia());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefoneContato(fornecedorDTO.getTelefoneContato());
        fornecedor.setEmailContato(fornecedorDTO.getEmailContato());

        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return FornecedorDTO.of(fornecedor);
    }

    private void validate(FornecedorDTO fornecedorDTO){

        LOGGER.info("Validando fornecedor");

        // VARIÁVEL DO TELEFONE INFORMADO
        String telefone = fornecedorDTO.getTelefoneContato();

        // CONDICIONAL DE TELEFONE TIPO CELULAR SOMENTE
        if(telefone.charAt(5) != '9'){
            throw new IllegalArgumentException("Informe apenas telefones celulares");
        }

        if(telefone.length() == 9){
            throw new IllegalArgumentException("Informe DDD e DDI");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())){
            throw new IllegalArgumentException("Razao Social não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("Cnpj não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())){
            throw new IllegalArgumentException("Nome fantasia não deve ser nulo/vazio");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getEndereco())){
            throw new IllegalArgumentException("Endereço não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getTelefoneContato() == null){
            throw new IllegalArgumentException("Telefone não deve ser nulo");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getEmailContato())){
            throw new IllegalArgumentException("Email não deve ser nulo/vazio");
        }
    }

    public FornecedorDTO findById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            Fornecedor fornecedor = fornecedorOptional.get();
            FornecedorDTO fornecedorDTO = FornecedorDTO.of(fornecedor);

            return fornecedorDTO;
        }

        /*String nome = "Gustavo";
        String empresa = "HBSIS";
        String mensagem = "meu nome é " + nome + " e eu trabalho na " + empresa + ".";

        String format1 = String.format("Meu nome é %s e eu trabalho na %s", nome, empresa);*/

        String format = String.format("ID %s não existe", id);

        throw new IllegalArgumentException(format);
    }

    public FornecedorDTO update(FornecedorDTO fornecedorDTO, Long id) {

        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorExistenteOptional.isPresent()){

            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payaload: {}", fornecedorDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);

            fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedorExistente.setCnpj(fornecedorDTO.getCnpj());
            fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
            fornecedorExistente.setTelefoneContato(fornecedorDTO.getTelefoneContato());
            fornecedorExistente.setEmailContato(fornecedorDTO.getEmailContato());

            fornecedorExistente = this.iFornecedorRepository.save(fornecedorExistente);

            return FornecedorDTO.of(fornecedorExistente);
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));

    }

    public void delete(Long id){

        LOGGER.info("Executando delete para usuário de ID: [{}]", id);

        this.iFornecedorRepository.deleteById(id);
    }

    // ENCONTRAR PELO CNPJ ESPECÍFICO
    public FornecedorDTO findByCnpj(String cnpj){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findByCnpj(cnpj);

        if(fornecedorOptional.isPresent()){
            Fornecedor fornecedor = fornecedorOptional.get();
            FornecedorDTO fornecedorDTO = FornecedorDTO.of(fornecedor);

            return fornecedorDTO;
        }

        String format = String.format("Cnpj %s não existe", cnpj);

        throw new IllegalArgumentException(format);
    }

}
