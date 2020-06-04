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

    public PedidoDTO salvar(PedidoDTO pedidoDTO) {

        this.validarPedido(pedidoDTO);

        LOGGER.info("Executando save de pedido");

        PedidoDTO pedidoDTOComParametros = gerarParametros(pedidoDTO);
        FornecedorDTO fornecedorDTO = fornecedorService.findById(pedidoDTO.getIdFornecedor());

        FuncionarioDTO funcionarioDTO = funcionarioService.findById(pedidoDTO.getIdFuncionario());

        Pedido pedido = new Pedido(
                pedidoDTOComParametros.getCodigoPedido(),
                new Funcionario(funcionarioDTO.getId()),
                Fornecedor.of(fornecedorDTO),
                pedidoDTO.getPrecoTotal(),
                pedidoDTOComParametros.getStatus()
        );

        //pedido.setDataRegistro(pedidoDTOComParametros.getDataRegistro());

        pedido = this.iPedidoRepositoy.save(pedido);
        return PedidoDTO.of(pedido);
    }

    public void validarPedido(PedidoDTO pedidoDTO){

        LOGGER.info("Validando pedido");

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
            LOGGER.info(String.format("Status de [%s] está correto", status));
        }else{
            throw new IllegalArgumentException("O status deve ser apenas ATIVO, CANCELADO ou RETIRADO");
        }
    }

    public PedidoDTO gerarParametros(PedidoDTO pedidoDTO) {

        int max1 = 9999;
        int max2 = 999;
        int min = 1;

        double gerador = Math.random();

        int codigoAleatorio01 = (int) Math.round(min + gerador * max1);
        int codigoAleatorio02 = (int) Math.round(min + gerador * max2);

        String codigoZerosEsquerda1 = StringUtils.leftPad(String.valueOf(codigoAleatorio01), 4, "0");
        String codigoZerosEsquerda2 = StringUtils.leftPad(String.valueOf(codigoAleatorio02), 3, "0");

        String codigo = "PED".concat(codigoZerosEsquerda1).concat(codigoZerosEsquerda2);
        pedidoDTO.setCodigoPedido(codigo);

        LocalDate data = LocalDate.now();
        pedidoDTO.setDataRegistro(data);

//        int dia = data.getDayOfMonth();
//        int mes = data.getMonthValue();
//        int ano = data.getYear();
//        String dataSla = ano + "-" + mes + "-" + dia;

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date dataRegistro = format.parse(dataSla);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String statusUpper = pedidoDTO.getStatus().toUpperCase();
        pedidoDTO.setStatus(statusUpper);

        return pedidoDTO;
    }
}


