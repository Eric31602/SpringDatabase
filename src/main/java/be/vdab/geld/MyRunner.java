package be.vdab.geld;

import be.vdab.geld.mensen.Mens;
import be.vdab.geld.mensen.MensService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyRunner implements CommandLineRunner {
    private final MensService mensService;

    public MyRunner(MensService mensService) {
        this.mensService = mensService;
    }

    @Override
    public void run(String... args) {
        var naam = IO.readln("Naam: ");
        var geld = new BigDecimal(IO.readln("Geld: "));
        var mens = new Mens(0, naam, geld);
        var nieuweId = mensService.create(mens);
        IO.println("Id van deze mens: " + nieuweId);
    }


}
