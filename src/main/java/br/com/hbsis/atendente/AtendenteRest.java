package br.com.hbsis.atendente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/atendente")
public class AtendenteRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtendenteRest.class);
    private final AtendenteService  atendenteService;

    @Autowired /** CONSTRUTOR */
    public AtendenteRest(AtendenteService atendenteService) {
        this.atendenteService = atendenteService;
    }

//    @PostMapping /** MÉTODOS */
//    public AtendenteDTO save(@RequestParam("file") MultipartFile file){
//
//        LOGGER.info("Recebendo save para atendente... nome do file: ", file.getOriginalFilename());
//        LOGGER.debug("Payload... nome do file: {}", file.getName());
//
//        //files.add(file.getOriginalFilename());
//        return this.atendenteService.save(file);
//    }
}
