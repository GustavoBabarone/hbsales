package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.Optional;

@Service
public class FornecedorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedorRepository iFornecedorRepository;

    @Autowired
    public FornecedorService(IFornecedorRepository iFornecedorRepository) {
        this.iFornecedorRepository = iFornecedorRepository;
    }

    public FornecedorDTO salvar(FornecedorDTO fornecedorDTO){

        this.validarFornecedor(fornecedorDTO);

        LOGGER.info("Executando save de fornecedor");

        Fornecedor fornecedor = new Fornecedor(
                fornecedorDTO.getRazaoSocial(),
                fornecedorDTO.getCnpj(),
                fornecedorDTO.getNomeFantasia(),
                fornecedorDTO.getEndereco(),
                fornecedorDTO.getTelefoneContato(),
                fornecedorDTO.getEmailContato()
        );

        fornecedor = this.iFornecedorRepository.save(fornecedor);
        return FornecedorDTO.of(fornecedor);
    }

    public FornecedorDTO atualizar(FornecedorDTO fornecedorDTO, Long id) {

        this.validarFornecedor(fornecedorDTO);

        Optional<Fornecedor> fornecedorExistenteOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorExistenteOptional.isPresent()){

            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Executando update de fornecedor de id: [{}]", id);

            fornecedorExistente.setRazaoSocial(fornecedorDTO.getRazaoSocial());
            fornecedorExistente.setCnpj(fornecedorDTO.getCnpj());
            fornecedorExistente.setNomeFantasia(fornecedorDTO.getNomeFantasia());
            fornecedorExistente.setEndereco(fornecedorDTO.getEndereco());
            fornecedorExistente.setTelefoneContato(fornecedorDTO.getTelefoneContato());
            fornecedorExistente.setEmailContato(fornecedorDTO.getEmailContato());

            fornecedorExistente = this.iFornecedorRepository.save(fornecedorExistente);
            return FornecedorDTO.of(fornecedorExistente);
        }

        throw new IllegalArgumentException(String.format("Fornecedor de id [%s] não encontrado", id));
    }

    public void deletar(Long id){

        LOGGER.info("Executando delete para fornecedor de id: [{}]", id);

        if(iFornecedorRepository.existsById(id)) {
            this.iFornecedorRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException(String.format("Fornecedor de id [%s] não encontrado", id));
        }
    }

    public FornecedorDTO findById(Long id){

        LOGGER.info("Executando findById para fornecedor de id: [{}]", id);

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findById(id);

        if(fornecedorOptional.isPresent()){
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("Fornecedor de id [%s] não encontrado", id));
    }

    public FornecedorDTO findByCnpj(String cnpj){

        LOGGER.info("Executando findByCnpj para fornecedor de cnpj: [{}]", cnpj);

        Optional<Fornecedor> fornecedorOptional = this.iFornecedorRepository.findByCnpj(cnpj);

        if(fornecedorOptional.isPresent()){
            return FornecedorDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("Fornecedor de cnpj [%s] não encontrado", cnpj));
    }

    private void validarFornecedor(FornecedorDTO fornecedorDTO){

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

    public String formatarCnpj(String cnpj){

        try {
            MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
            mask.setValueContainsLiteralCharacters(false);
            cnpj = mask.valueToString(cnpj);
        } catch (ParseException ex) {
            LOGGER.info("ERRO! Padrão de cnpj inválido");
        }

        return cnpj;
    }

    public String desformatarCnpj(String cnpj) {
        return cnpj
                .replace(".", "")
                .replace("/", "")
                .replace("-", "");
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

}
