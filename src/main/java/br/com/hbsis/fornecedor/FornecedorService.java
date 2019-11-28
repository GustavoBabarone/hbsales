package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classe responsável pelo processamento da regra de negócio
 */
@Service
public class FornecedorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);

    private final IFornecedorRepository iFornecedorRepository;

    public FornecedorService(IFornecedorRepository iFornecedorRepository) { this.iFornecedorRepository = iFornecedorRepository; }

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

        if(StringUtils.isEmpty(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("FornecedorDTO não deve ser nulo/vazio");
        }

    }

    public FornecedorDTO FindById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    /* ENCONTRAR POR ID DE FORNECEDOR - COMEÇO */
    public FornecedorDTO findIdFornecedor(Long id){

        // LISTA ?
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("Fornecedor não existe ",  id));

    }
    /* FINAL */

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

}
