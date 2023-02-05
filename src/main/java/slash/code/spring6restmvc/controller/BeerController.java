package slash.code.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import slash.code.spring6restmvc.services.BeerService;

@Controller
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;


}
