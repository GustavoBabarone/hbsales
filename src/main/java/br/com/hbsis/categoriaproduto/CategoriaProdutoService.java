package br.com.hbsis.categoriaproduto;

import br.com.hbsis.csv.CSV;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProdutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaProdutoService.class);
    private final ICategoriaProdutoRepository iCategoriaProdutoRepository;
    private final FornecedorService fornecedorService;
    private final CSV csv;

    @Autowired
    public CategoriaProdutoService(ICategoriaProdutoRepository iCategoriaProdutoRepository, FornecedorService fornecedorService, CSV csv) {
        this.iCategoriaProdutoRepository = iCategoriaProdutoRepository;
        this.fornecedorService = fornecedorService;
        this.csv = csv;
    }

    public CategoriaProdutoDTO salvar(CategoriaProdutoDTO categoriaProdutoDTO) {

        this.validarCategoria(categoriaProdutoDTO);

        LOGGER.info("Executando save de categoria");

        FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());
        String codigo = gerarCodigo(categoriaProdutoDTO.getCodigoCategoria(), fornecedorDTO.getCnpj());

        CategoriaProduto categoriaProduto = new CategoriaProduto(
                codigo,
                Fornecedor.of(fornecedorDTO),
                categoriaProdutoDTO.getNome()
        );

        categoriaProduto = this.iCategoriaProdutoRepository.save(categoriaProduto);
        return CategoriaProdutoDTO.of(categoriaProduto);
    }

    public CategoriaProdutoDTO atualizar(CategoriaProdutoDTO categoriaProdutoDTO, Long id) {

        this.validarCategoria(categoriaProdutoDTO);

        Optional<CategoriaProduto> categoriaProdutoExistenteOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoExistenteOptional.isPresent()) {

            CategoriaProduto categoriaProdutoExistente = categoriaProdutoExistenteOptional.get();

            LOGGER.info("Executando update de categoria de id: [{}]", categoriaProdutoExistente.getId());

            FornecedorDTO fornecedorDTO = fornecedorService.findById(categoriaProdutoDTO.getIdFornecedor());

            categoriaProdutoExistente.setNome(categoriaProdutoDTO.getNome());
            categoriaProdutoExistente.setFornecedor(Fornecedor.of(fornecedorDTO));
            categoriaProdutoExistente.setCodigoCategoria(
                    gerarCodigo(categoriaProdutoDTO.getCodigoCategoria(), fornecedorDTO.getCnpj())
            );

            categoriaProdutoExistente = this.iCategoriaProdutoRepository.save(categoriaProdutoExistente);
            return CategoriaProdutoDTO.of(categoriaProdutoExistente);
        }

        throw new IllegalArgumentException(String.format("Categoria de id [%s] não encontrada", id));
    }

    public void deletar(Long id) {

        LOGGER.info("Executando delete para categoria de id: [{}]", id);

        if(iCategoriaProdutoRepository.existsById(id)){
            this.iCategoriaProdutoRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException(String.format("Categoria de id [%s] não encontrada", id));
        }
    }

    public CategoriaProdutoDTO findById(Long id) {

        LOGGER.info("Executando findById para categoria de id: [{}]", id);

        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findById(id);

        if (categoriaProdutoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("Categoria de id [%s] não encontrada", id));
    }

    private void validarCategoria(CategoriaProdutoDTO categoriaProdutoDTO) {

        LOGGER.info("Validando categoria");

        if(categoriaProdutoDTO == null){
            throw new IllegalArgumentException("CategoriaProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getCodigoCategoria())) {
            throw new IllegalArgumentException("Código não deve ser nulo/vazio");
        }

        if (categoriaProdutoDTO.getCodigoCategoria().length() > 3) {
            throw new IllegalArgumentException("Código deve conter até 3 digitos");
        }

        if (categoriaProdutoDTO.getIdFornecedor() == null) {
            throw new IllegalArgumentException("Id do fornecedor não deve ser nulo");
        }

        if (StringUtils.isEmpty(categoriaProdutoDTO.getNome())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }

        if (categoriaProdutoDTO.getNome().length() > 50) {
            throw new IllegalArgumentException("Nome deve conter até 50 caracteres");
        }
    }

    public CategoriaProdutoDTO findByCodigoCategoria(String codigo) {

        LOGGER.info("Executando findByCodigo para categoria de codigo: [{}]", codigo);

        Optional<CategoriaProduto> categoriaProdutoOptional = this.iCategoriaProdutoRepository.findByCodigoCategoria(codigo);

        if (categoriaProdutoOptional.isPresent()) {
            return CategoriaProdutoDTO.of(categoriaProdutoOptional.get());
        }

        throw new IllegalArgumentException(String.format("Categoria de codigo [%s] não encontrada", codigo));
    }

    public boolean existByCodigo(String codigo) {
        return this.iCategoriaProdutoRepository.existsByCodigoCategoria(codigo);
    }

    public String gerarCodigo(String codigo, String cnpj){

        codigo = codigo.toUpperCase();
        String zerosEsquerda = StringUtils.leftPad(codigo, 3, "0");
        String digitosCnpj = cnpj.substring(cnpj.length() - 4);
        return "CAT" + digitosCnpj + zerosEsquerda;
    }

    public void exportarCategoria(HttpServletResponse response) throws Exception {

        String arquivoCSV = "categoriaProduto.csv";
        String[] cabecalhoCSV = {"codigo", "nome", "razao_social", "cnpj"};
        ICSVWriter icsvWriter = csv.padraoExportarCsv(response, arquivoCSV, cabecalhoCSV);

        for(CategoriaProduto rows : iCategoriaProdutoRepository.findAll()){

            icsvWriter.writeNext(new String[]{
                    rows.getCodigoCategoria(),
                    rows.getNome(),
                    rows.getFornecedor().getRazaoSocial(),
                    fornecedorService.formatarCnpj(rows.getFornecedor().getCnpj()),
            });
        }
        LOGGER.info("Finalizando exportação de produto...");
    }

    public List<CategoriaProduto> importarCategoria(MultipartFile file) throws Exception {

        List<String[]> row = csv.padraoImportarCsv(file);
        List<CategoriaProduto> categoriaProdutoList = new ArrayList<>();

        for(String[] linha : row){

            String[] vetor = linha[0].replaceAll("\"", "").split(";");

            CategoriaProduto categoriaProduto = new CategoriaProduto();

            if(existByCodigo(vetor[0])) {

                categoriaProduto.setCodigoCategoria(vetor[0]);
                categoriaProduto.setNome(vetor[1]);

                String cnpjDesformatado = fornecedorService.desformatarCnpj(vetor[3]);
                FornecedorDTO fornecedorDTO = fornecedorService.findByCnpj(cnpjDesformatado);
                Fornecedor fornecedor = Fornecedor.of(fornecedorDTO);
                categoriaProduto.setFornecedor(fornecedor);

                categoriaProdutoList.add(categoriaProduto);

            }else {
                LOGGER.info("Categoria já existente no banco de dados...");
            }
        }
        LOGGER.info("Finalizando importação de categoria...");
        return iCategoriaProdutoRepository.saveAll(categoriaProdutoList);
    }

    /** CSV - IMPORTAR POR FORNECEDOR - ATIVIDADE 11 */
    public CategoriaProduto executarSaveNaRepository(CategoriaProduto categoriaProduto){
        return this.iCategoriaProdutoRepository.save(categoriaProduto);
    }

    public Optional<CategoriaProduto> findByCodigoCategoriaImportProdutoPorFornecedor(String codigoCategoria){
        return this.iCategoriaProdutoRepository.findByCodigoCategoria(codigoCategoria);
    }
}
