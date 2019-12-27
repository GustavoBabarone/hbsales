package br.com.hbsis.periodovendas;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PeriodoVendasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasService.class);
    private final FornecedorService fornecedorService;
    private final IPeriodoVendasRepository iPeriodoVendasRepository;

    @Autowired /** CONSTRUTOR */
    public PeriodoVendasService(FornecedorService fornecedorService, IPeriodoVendasRepository iPeriodoVendasRepository) {
        this.fornecedorService = fornecedorService;
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
    }

    public PeriodoVendasDTO save(PeriodoVendasDTO periodoVendasDTO){

        this.validate(periodoVendasDTO);

        LOGGER.info("Salvando periodo de vendas...");
        LOGGER.debug("Periodo de Vendas: {}", periodoVendasDTO);

        PeriodoVendas periodoVendas = new PeriodoVendas();

        periodoVendas.setDataInicio(periodoVendasDTO.getDataInicio());
        periodoVendas.setDataFim(periodoVendasDTO.getDataFim());
        periodoVendas.setDataRetirada(periodoVendasDTO.getDataRetirada());

        FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());
        Fornecedor fornecedor = conversor(fornecedorDTO);
        periodoVendas.setFornecedor(fornecedor);

        periodoVendas.setDescricao(periodoVendasDTO.getDescricao());

        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);

        return PeriodoVendasDTO.of(periodoVendas);
    }

    public void validate(PeriodoVendasDTO periodoVendasDTO){

        LOGGER.info("Validando periodo de vendas...");

        if(periodoVendasDTO == null){
            throw new IllegalArgumentException("PeriodoVendasDTO não deve ser nulo.");
        }

        if(periodoVendasDTO.getDataInicio() == null){
            throw new IllegalArgumentException("Data de inicio não deve ser nula.");
        }

        if(periodoVendasDTO.getDataFim() == null){
            throw new IllegalArgumentException("Data de fim não deve ser nula.");
        }

        if(periodoVendasDTO.getDataRetirada() == null){
            throw new IllegalArgumentException("Data de retirada não deve se nula.");
        }

        if(periodoVendasDTO.getIdFornecedor() == null){
            throw new IllegalArgumentException("Id do fornecedor não deve ser nulo.");
        }

        if(StringUtils.isEmpty(periodoVendasDTO.getDescricao())){
            throw new IllegalArgumentException("Descricao não deve ser nula/vazia.");
        }

        if(periodoVendasDTO.getDataFim().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de fim não pode ser anterior a hoje.");
        }

        if(periodoVendasDTO.getDataInicio().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Data de inicio não pode ser anterior a hoje.");
        }

        if(periodoVendasDTO.getDataFim().isBefore(periodoVendasDTO.getDataInicio())){
            throw new IllegalArgumentException("Data de fim não pode ser anterior a data de inicio.");
        }

        if(iPeriodoVendasRepository.existeDataAberta(periodoVendasDTO.getDataInicio(), periodoVendasDTO.getIdFornecedor()) >= 1){
            throw new IllegalArgumentException("Há outro periodo de vendas em aberto!");
        }

    }

    public Fornecedor conversor(FornecedorDTO fornecedorDTO){

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorDTO.getId());

        return fornecedor;
    }

    public void delete(Long id){

        LOGGER.info("Executando delete para periodo de vendas de id: [{}]", id);

        this.iPeriodoVendasRepository.deleteById(id);
    }

    public PeriodoVendasDTO findById(Long id){

        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasOptional.isPresent()){

            PeriodoVendas periodoVendas = periodoVendasOptional.get();
            PeriodoVendasDTO periodoVendasDTO = PeriodoVendasDTO.of(periodoVendas);
            return periodoVendasDTO;
        }

        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    public PeriodoVendasDTO update(PeriodoVendasDTO periodoVendasDTO, Long id){

        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasExistenteOptional.isPresent()){

            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            LOGGER.info("Atualizando periodo... id: [{}]", periodoVendasDTO.getId());
            LOGGER.debug("Payload: {}", periodoVendasDTO);
            LOGGER.debug("Periodo de vendas existente: {}", periodoVendasExistente);

            periodoVendasExistente.setDataInicio(periodoVendasDTO.getDataInicio());
            periodoVendasExistente.setDataFim(periodoVendasDTO.getDataFim());
            periodoVendasExistente.setDataRetirada(periodoVendasDTO.getDataRetirada());

            FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());
            Fornecedor fornecedor = conversor(fornecedorDTO);
            periodoVendasExistente.setFornecedor(fornecedor);

            periodoVendasExistente.setDescricao(periodoVendasDTO.getDescricao());

            periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);

            return PeriodoVendasDTO.of(periodoVendasExistente);
        }

        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }
}
