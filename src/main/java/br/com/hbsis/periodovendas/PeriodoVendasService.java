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
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoVendasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendasService.class);
    private final FornecedorService fornecedorService;
    private final IPeriodoVendasRepository iPeriodoVendasRepository;

    @Autowired
    public PeriodoVendasService(FornecedorService fornecedorService, IPeriodoVendasRepository iPeriodoVendasRepository) {
        this.fornecedorService = fornecedorService;
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
    }

    public PeriodoVendasDTO salvar(PeriodoVendasDTO periodoVendasDTO){

        this.validarPeriodoVendas(periodoVendasDTO);

        LOGGER.info("Executando save de periodo de vendas");

        FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());

        PeriodoVendas periodoVendas = new PeriodoVendas(
                periodoVendasDTO.getDataInicio(),
                periodoVendasDTO.getDataFim(),
                Fornecedor.of(fornecedorDTO),
                periodoVendasDTO.getDataRetirada(),
                periodoVendasDTO.getDescricao()
        );

        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);
        return PeriodoVendasDTO.of(periodoVendas);
    }

    public PeriodoVendasDTO atualizar(PeriodoVendasDTO periodoVendasDTO, Long id){

        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasExistenteOptional.isPresent()){

            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            LOGGER.info("Executando update de periodo de vendas de id: [{}]", periodoVendasExistente.getId());

            FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());

            periodoVendasExistente.setDataInicio(periodoVendasDTO.getDataInicio());
            periodoVendasExistente.setDataFim(periodoVendasDTO.getDataFim());
            periodoVendasExistente.setFornecedor(Fornecedor.of(fornecedorDTO));
            periodoVendasExistente.setDataRetirada(periodoVendasDTO.getDataRetirada());
            periodoVendasExistente.setDescricao(periodoVendasDTO.getDescricao());

            periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);
            return PeriodoVendasDTO.of(periodoVendasExistente);
        }

        throw new IllegalArgumentException(String.format("Periodo de vendas de id [%s] não encontrado", id));
    }

    public void deletar(Long id){

        LOGGER.info("Executando delete para periodo de vendas de id: [{}]", id);

        if(iPeriodoVendasRepository.existsById(id)){
            this.iPeriodoVendasRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException(String.format("Periodo de vendas de id [%s] não encontrado", id));
        }
    }

    public PeriodoVendasDTO findById(Long id){

        LOGGER.info("Executando findById para periodo de vendas de id: [{}]", id);

        Optional<PeriodoVendas> periodoVendasOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasOptional.isPresent()){
            return PeriodoVendasDTO.of(periodoVendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("Periodo de vendas de id [%s] não encontrado", id));
    }

    public void validarPeriodoVendas(PeriodoVendasDTO periodoVendasDTO){

        LOGGER.info("Validando periodo de vendas");

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

        validarDatasVigencia(periodoVendasDTO);
    }

    public void validarDatasVigencia(PeriodoVendasDTO periodoVendasDTO) {

        LOGGER.info("Validando datas de vigencia do periodo de vendas");

        List<PeriodoVendas> periodoVendasList = this.iPeriodoVendasRepository.findAllByFornecedor_Id(periodoVendasDTO.getIdFornecedor());

        if (periodoVendasDTO.getDataFim().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior a hoje.");
        }

        if (periodoVendasDTO.getDataInicio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de inicio não pode ser anterior a hoje.");
        }

        if (periodoVendasDTO.getDataFim().isBefore(periodoVendasDTO.getDataInicio())) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior a data de inicio.");
        }

        for(PeriodoVendas periodoVendas : periodoVendasList){

            if (periodoVendasDTO.getDataInicio().isAfter(periodoVendas.getDataInicio()) && periodoVendasDTO.getDataFim().isBefore(periodoVendas.getDataFim())) {
                throw new IllegalArgumentException("Periodo inválido");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataInicio()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataFim())) {
                throw new IllegalArgumentException("Periodo inválido");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataInicio()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataInicio())) {
                throw new IllegalArgumentException("Periodo inválido");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataFim()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataFim())) {
                throw new IllegalArgumentException("Periodo inválido");
            }
        }
    }
}
