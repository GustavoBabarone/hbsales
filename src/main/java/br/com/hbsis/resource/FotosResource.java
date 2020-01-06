package br.com.hbsis.resource;

import br.com.hbsis.storage.Disco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fotos")
public class FotosResource {

    @Autowired
    private Disco disco;

    private static final Logger LOGGER = LoggerFactory.getLogger(FotosResource.class);

    @PostMapping("/{nome}/{email}/{senha}")
    public void upload(@RequestParam("file") MultipartFile file, @PathVariable("nome") String nome, @PathVariable("email") String email,
                       @PathVariable("senha") String senha){

        LOGGER.info("Recebendo upload e save do atendente...");
        disco.salvarFoto(file, nome, email, senha);
    }
}
