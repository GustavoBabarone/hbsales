package br.com.hbsis.resource;

import br.com.hbsis.storage.Disco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fotos")
public class FotosResource {

    @Autowired
    private Disco disco;

    @PostMapping
    public void upload(@RequestParam("file") MultipartFile file){
        disco.salvarFoto(file);
    }
}
