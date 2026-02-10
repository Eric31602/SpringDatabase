package be.vdab.geld;

import be.vdab.geld.mensen.MensNietGevondenException;
import be.vdab.geld.mensen.MensService;
import be.vdab.geld.mensen.OnvoldoendeGeldException;
import be.vdab.geld.mensen.Schenking;
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
        var vanMensId = Long.parseLong(IO.readln("Id van mens: "));
        var aanMensId = Long.parseLong(IO.readln("Id aan mens: "));
        var bedrag = new BigDecimal(IO.readln("Bedrag: "));

        try {
            var schenking = new Schenking(vanMensId, aanMensId, bedrag);
            mensService.schenk(schenking);
            IO.println("Schenking gelukt");
        } catch (IllegalArgumentException ex) {
            IO.println(ex.getMessage());
        } catch (MensNietGevondenException ex) {
            IO.println("Schenking mislukt. Mens ontbreekt. Id: " + ex.getId());
        } catch (OnvoldoendeGeldException ex) {
            IO.println("Schenking mislukt. Onvoldoende geld.");
        }
    }

}
