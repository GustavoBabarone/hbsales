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

    @Autowired /** CONSTRUTOR */
    public PeriodoVendasService(FornecedorService fornecedorService, IPeriodoVendasRepository iPeriodoVendasRepository) {
        this.fornecedorService = fornecedorService;
        this.iPeriodoVendasRepository = iPeriodoVendasRepository;
    }

    /** MÉTODOS DE CRUD */
    public PeriodoVendasDTO salvar(PeriodoVendasDTO periodoVendasDTO){

        this.validarCamposTexto(periodoVendasDTO);

        LOGGER.info("Salvando periodo de vendas...");
        LOGGER.debug("Periodo de Vendas: {}", periodoVendasDTO);

        PeriodoVendas periodoVendas = new PeriodoVendas();
        periodoVendas.setDataInicio(periodoVendasDTO.getDataInicio());
        periodoVendas.setDataFim(periodoVendasDTO.getDataFim());
        periodoVendas.setDataRetirada(periodoVendasDTO.getDataRetirada());
        periodoVendas.setDescricao(periodoVendasDTO.getDescricao());

        FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());
        periodoVendas.setFornecedor(Fornecedor.of(fornecedorDTO));

        periodoVendas = this.iPeriodoVendasRepository.save(periodoVendas);
        return PeriodoVendasDTO.of(periodoVendas);
    }

    public PeriodoVendasDTO atualizar(PeriodoVendasDTO periodoVendasDTO, Long id){

        Optional<PeriodoVendas> periodoVendasExistenteOptional = this.iPeriodoVendasRepository.findById(id);

        if(periodoVendasExistenteOptional.isPresent()){

            PeriodoVendas periodoVendasExistente = periodoVendasExistenteOptional.get();

            LOGGER.info("Atualizando periodo... id: [{}]", periodoVendasDTO.getId());
            LOGGER.debug("Payload: {}", periodoVendasDTO);
            LOGGER.debug("Periodo de vendas existente: {}", periodoVendasExistente);

            periodoVendasExistente.setDataInicio(periodoVendasDTO.getDataInicio());
            periodoVendasExistente.setDataFim(periodoVendasDTO.getDataFim());
            periodoVendasExistente.setDataRetirada(periodoVendasDTO.getDataRetirada());
            periodoVendasExistente.setDescricao(periodoVendasDTO.getDescricao());

            FornecedorDTO fornecedorDTO = fornecedorService.findById(periodoVendasDTO.getIdFornecedor());
            periodoVendasExistente.setFornecedor(Fornecedor.of(fornecedorDTO));

            periodoVendasExistente = this.iPeriodoVendasRepository.save(periodoVendasExistente);
            return PeriodoVendasDTO.of(periodoVendasExistente);
        }
        throw new IllegalArgumentException(String.format("Id %s não existe", id));
    }

    public void deletar(Long id){

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

    public void validarCamposTexto(PeriodoVendasDTO periodoVendasDTO){

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

        /** VALIDAÇÃO DAS DATAS */
        validarDatasInicioFim(periodoVendasDTO);
    }

    public void validarDatasInicioFim(PeriodoVendasDTO periodoVendasDTO) {

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
                throw new IllegalArgumentException("Caso 01");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataInicio()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataFim())) {
                throw new IllegalArgumentException("Caso 02");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataInicio()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataInicio())) {
                throw new IllegalArgumentException("Caso 03");
            }

            if (periodoVendasDTO.getDataInicio().isBefore(periodoVendas.getDataFim()) && periodoVendasDTO.getDataFim().isAfter(periodoVendas.getDataFim())) {
                throw new IllegalArgumentException("Caso 04");
            }
        }
    }
}
