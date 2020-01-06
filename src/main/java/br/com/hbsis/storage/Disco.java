package br.com.hbsis.storage;

import br.com.hbsis.atendente.AtendenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Disco {

    private static final Logger LOGGER = LoggerFactory.getLogger(Disco.class);
    private final AtendenteService atendenteService;

    @Autowired
    public Disco(AtendenteService atendenteService) {
        this.atendenteService = atendenteService;
    }

    @Value("${hbsis.disco.raiz}")
    private String raiz;

    @Value("${hbsis.disco.diretorio-fotos}")
    private String diretorioFotos;

    public void salvarFoto(MultipartFile foto, String nome, String email, String senha){

        atendenteService.save(foto, nome, email, senha);
        this.salvar(this.diretorioFotos, foto);
    }

    public void salvar(String diretorio, MultipartFile arquivo){

        LOGGER.info("Executando upload da imagem...");

        /** DIRETÓRIO ONDE O ARQUIVO VAI SER SALVO */
        Path diretorioPath = Paths.get(this.raiz, diretorio);

        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());

        /** TENTATIVA */
        try{

            /** CRIAR OS DIRETÓRIOS  */
            Files.createDirectories(diretorioPath);

            /** ATRIBUIR CAMINHO ONDE O ARQUIVO SERÁ SALVO */
            arquivo.transferTo(arquivoPath.toFile());

            LOGGER.info("Finalização do upload...");

        }catch (IOException e){

            /** EM CASO DE ERRO */
            throw new RuntimeException("Problema na tentativa de salvar arquivo: "+e);
        }
    }
}
