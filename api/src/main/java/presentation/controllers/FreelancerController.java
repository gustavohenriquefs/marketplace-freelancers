package presentation.controllers;

import domain.entities.Freelancer;
import domain.usecases.GetFreelancers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/freelancers")
public class FreelancerController {
    private GetFreelancers _getFreelancers;

    public  FreelancerController(GetFreelancers getFreelancers) {
        this._getFreelancers = getFreelancers;
    }

    @PostMapping("/")
    public ResponseEntity<List<Freelancer>> get() {
        List<Freelancer> freelancers = this._getFreelancers.getFreelancers();
        return ResponseEntity.ok().body(freelancers);
    }
}
