package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FornecedorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedorRepository iFornecedorRepository;

    @Autowired /** CONTRUTOR */
    public FornecedorService(IFornecedorRepository iFornecedorRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
    }

    /** MÉTODOS DE CRUD */
    public FornecedorDTO salvar(FornecedorDTO fornecedorDTO){

        this.validarCamposTexto(fornecedorDTO);

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

    public FornecedorDTO atualizar(FornecedorDTO fornecedorDTO, Long id) {

        this.validarCamposTexto(fornecedorDTO);

        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorExistenteOptional.isPresent()){

            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando br.com.hbsis.Fornecedor... nome: [{}]", Fornecedor.class.getName());
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

    public void deletar(Long id){

        LOGGER.info("Executando delete para fornecedor de id: [{}]", id);

        if(iFornecedorRepository.existsById(id)) {
            this.iFornecedorRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("Fornecedor não cadastrado");
        }
    }

    public FornecedorDTO findById(Long id){

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){

            Fornecedor fornecedor = fornecedorOptional.get();
            FornecedorDTO fornecedorDTO = FornecedorDTO.of(fornecedor);
            return fornecedorDTO;
        }
        String format = String.format("ID %s não existe", id);
        throw new IllegalArgumentException(format);
    }

    private void validarCamposTexto(FornecedorDTO fornecedorDTO){

        LOGGER.info("Validando fornecedor");

        String telefone = fornecedorDTO.getTelefoneContato();
        if(telefone.charAt(5) != '9'){
            throw new IllegalArgumentException("Informe apenas telefones celulares");
        }

        if(telefone.length() == 9){
            throw new IllegalArgumentException("Informe DDD e DDI");
        }

        if(telefone.length() != 13){
            throw new IllegalArgumentException("Telefone deve conter 13 digitos");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getRazaoSocial())){
            throw new IllegalArgumentException("Razao Social não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getRazaoSocial().length() > 100){
            throw new IllegalArgumentException("Razao Social deve conter até 100 caracteres");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getCnpj())){
            throw new IllegalArgumentException("Cnpj não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getCnpj().length() != 14){
            throw new IllegalArgumentException("Cnpj deve conter 14 digitos");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getNomeFantasia())){
            throw new IllegalArgumentException("Nome fantasia não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getNomeFantasia().length() > 100){
            throw new IllegalArgumentException("Nome fantasia deve conter até 100 caracteres");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getEndereco())){
            throw new IllegalArgumentException("Endereço não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getEndereco().length() > 100){
            throw new IllegalArgumentException("Endereço deve conter até 100 caracteres");
        }

        if(fornecedorDTO.getTelefoneContato() == null){
            throw new IllegalArgumentException("Telefone não deve ser nulo");
        }

        if(fornecedorDTO.getTelefoneContato().length() != 13){
            throw new IllegalArgumentException("Telefone deve conter 13 digitos");
        }

        if(StringUtils.isEmpty(fornecedorDTO.getEmailContato())){
            throw new IllegalArgumentException("Email não deve ser nulo/vazio");
        }

        if(fornecedorDTO.getEmailContato().length() > 50){
            throw new IllegalArgumentException("Email deve conter até 50 caracteres");
        }
    }

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

    public boolean findByIdFornecedor(Long id){

        boolean valida;
        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            valida = true;
            return valida;
        }else {
            valida = false;
            return valida;
        }

    }

    /** FORMATAÇÕES GERAL */
    public Fornecedor converterObjeto(FornecedorDTO fornecedorDTO){

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorDTO.getId());
        return fornecedor;
    }
}
