package br.com.hbsis.pedido;

import br.com.hbsis.categoriaproduto.CategoriaProdutoService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioDTO;
import br.com.hbsis.funcionario.FuncionarioService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
public class PedidoService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    public final IPedidoRepositoy iPedidoRepositoy;
    public final CategoriaProdutoService categoriaProdutoService;
    public final FornecedorService fornecedorService;
    public final FuncionarioService funcionarioService;

    @Autowired
    public PedidoService(IPedidoRepositoy iPedidoRepositoy, CategoriaProdutoService categoriaProdutoService, FornecedorService fornecedorService, FuncionarioService funcionarioService) {
        this.iPedidoRepositoy = iPedidoRepositoy;
        this.categoriaProdutoService = categoriaProdutoService;
        this.fornecedorService = fornecedorService;
        this.funcionarioService = funcionarioService;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {

        this.validate(pedidoDTO);

        LOGGER.info("Salvando pedido...");
        LOGGER.debug("PedidoDTO: {}", pedidoDTO);

        Pedido pedido = new Pedido();

        PedidoDTO pedidoDTOparam = criarParametros(pedidoDTO);

        pedido.setCodigoPedido(pedidoDTOparam.getCodigoPedido());

        FornecedorDTO fornecedorDTO = fornecedorService.findById(pedidoDTO.getIdFornecedor());
        Fornecedor fornecedor = fornecedorService.converterObjeto(fornecedorDTO);
        pedido.setFornecedor(fornecedor);

        FuncionarioDTO funcionarioDTO = funcionarioService.findById(pedidoDTO.getIdFuncionario());
        Funcionario funcionario = funcionarioService.converterObjeto(funcionarioDTO);
        pedido.setFuncionario(funcionario);

        pedido.setPrecoTotal(pedidoDTO.getPrecoTotal());

        pedido.setStatus(pedidoDTOparam.getStatus());
        //pedido.setDataRegistro(pedidoDTOparam.getDataRegistro());

        pedido = this.iPedidoRepositoy.save(pedido);

        return PedidoDTO.of(pedido);
    }

    public void validate(PedidoDTO pedidoDTO){

        LOGGER.info("Validando informações pedido...");

        if(pedidoDTO == null){
            throw new IllegalArgumentException("Objeto PedidoDTO não deve ser nulo.");
        }

        if(pedidoDTO.getIdFuncionario() == null){
            throw new IllegalArgumentException("Id do funcionário não deve ser nulo.");
        }

        if(pedidoDTO.getIdFornecedor() == null){
            throw new IllegalArgumentException("Id do fornecedor não deve ser nulo");
        }

        String status = pedidoDTO.getStatus().toUpperCase();

        if(status.equals("ATIVO") || status.equals("CANCELADO") || status.equals("RETIRADO")){
            LOGGER.info(String.format("Status de %s está correto...", status));
        }else{
            throw new IllegalArgumentException("O status deve conter apenas ativo, cancelado ou retirado!");
        }
    }

    public PedidoDTO criarParametros(PedidoDTO pedidoDTO) {

        /* CÓDIGO */
        int max01 = 9999;
        int max02 = 999;
        int min = 1;
        double geradorAleatorio01 = Math.random();
        double geradorAleatorio02 = Math.random();
        int numeroAleatorio01 = (int) Math.round(min+geradorAleatorio01*max01);
        int numeroAleatorio02 = (int) Math.round(min+geradorAleatorio02*max02);
        String codigo01 = String.valueOf(numeroAleatorio01);
        String codigo02 = String.valueOf(numeroAleatorio02);
        String codigoProcessado01 = StringUtils.leftPad(codigo01, 4, "0");
        String codigoProcessado02 = StringUtils.leftPad(codigo02, 3, "0");
        String codConcat = "PED"+codigoProcessado01+codigoProcessado02;
        pedidoDTO.setCodigoPedido(codConcat);

        /* DATA DE REGISTRO */
        LocalDate data = LocalDate.now();
        int dia = data.getDayOfMonth();
        int mes = data.getMonthValue();
        int ano = data.getYear();
        String dataSla = ano+"-"+mes+"-"+dia;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dataRegistro = format.parse(dataSla);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* STATUS */
        String statusUpper = pedidoDTO.getStatus().toUpperCase();
        pedidoDTO.setStatus(statusUpper);

        return pedidoDTO;
    }
}


