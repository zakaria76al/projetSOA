package com.projet.soa.authentification.ressources;

import com.projet.soa.authentification.models.Fournisseur;
import com.projet.soa.authentification.models.Login;
import com.projet.soa.authentification.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthentificationController {

    @Autowired
    private FournisseurRepository repository;

    @PostMapping("/register")
    public String saveFournisseur(@RequestBody Fournisseur fournisseur){
        repository.save(fournisseur);
        final String uri = "http://localhost:8881/addFournisseur";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, fournisseur, String.class);

        System.out.println(result);
        return "Added with userName " + fournisseur.getUsername() + " And ID : " + fournisseur.getId();
    }

    @PostMapping("/login")
    public String login(@RequestBody Login login){
        List<Fournisseur> list = repository.findAll();
        for (Fournisseur a: list) {
            if(a.getUsername().equals(login.getUsername())){
                if(a.getMdp().equals(login.getMdp())){
                    return "Connexion done";
                }
                else
                {
                    return "Incorrect password";
                }
            }
        }
        return "Username not found";
    }

    @GetMapping("/findAllFournisseurs")
    public List<Fournisseur> getFournisseurs(){
        return repository.findAll();
    }

    @GetMapping("/findFournisseur/{id}")
    public Optional<Fournisseur> getFournisseur(@PathVariable int id){
        return repository.findById(id);
    }

    @GetMapping("/deleteFournisseur/{id}")
    public String deleteFournisseur(@PathVariable int id){
        repository.deleteById(id);
        return "Fournisseur deleted : " + id;
    }
}