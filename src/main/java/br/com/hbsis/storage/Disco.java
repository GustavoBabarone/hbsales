package br.com.hbsis.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Disco {

    @Value("${hbsis.disco.raiz}")
    private String raiz;

    @Value("${hbsis.disco.diretorio-fotos}")
    private String diretorioFotos;

    public void salvarFoto(MultipartFile foto){
        this.salvar(this.diretorioFotos, foto);
    }

    public void salvar(String diretorio, MultipartFile arquivo){

        /** DIRETÓRIO ONDE O ARQUIVO VAI SER SALVO */
        Path diretorioPath = Paths.get(this.raiz, diretorio);

        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());

        /** TENTATIVA */
        try{

            /** CRIAR OS DIRETÓRIOS  */
            Files.createDirectories(diretorioPath);

            /** ATRIBUIR CAMINHO ONDE O ARQUIVO SERÁ SALVO */
            arquivo.transferTo(arquivoPath.toFile());

        }catch (IOException e){

            /** EM CASO DE ERRO */
            throw new RuntimeException("Problema na tentativa de salvar arquivo: "+e);
        }
    }
}
