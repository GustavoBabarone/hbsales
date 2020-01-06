package br.com.hbsis.atendente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AtendenteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtendenteService.class);
    private final IAtendenteRepository iAtendenteRepository;

    @Autowired /** CONSTRUTOR */
    public AtendenteService(IAtendenteRepository iAtendenteRepository) {
        this.iAtendenteRepository = iAtendenteRepository;
    }

    /** MÉTODOS DE CRUD */
    public AtendenteDTO save(MultipartFile file, String nome, String email, String senha) {

        LOGGER.info("Salvando atendente");
        LOGGER.debug("Atendente... name: {}", file.getName());

        Atendente atendente = new Atendente();
        atendente.setNome(nome);
        atendente.setEmail(email);
        atendente.setSenha(senha);
        atendente.setFoto(file.getOriginalFilename());

        atendente = this.iAtendenteRepository.save(atendente);

        LOGGER.info("Finalizando save do atendente! DEU BOM");
        return AtendenteDTO.of(atendente);
    }
}
