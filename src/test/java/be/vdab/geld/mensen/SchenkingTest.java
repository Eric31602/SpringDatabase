package be.vdab.geld.mensen;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class SchenkingTest {
    @Test
    void eenSchenkingAanvaardtCorrecteParameters() {
        new Schenking(1, 2, BigDecimal.ONE);
    }
    @Test void vanMensIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Schenking(0, 2, BigDecimal.ONE));
    }
    @Test void aanMensIdMoetPositiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Schenking(1, 0, BigDecimal.ONE));
    }
    @Test void eenSchenkingAanJezelfMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Schenking(1, 1, BigDecimal.ONE));
    }
    @Test void eenSchenkingVan0€Mislukt() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Schenking(1, 2, BigDecimal.ZERO));
    }
    @Test void eenSchenkingMetEenNegatiefBedragMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Schenking(1, 2, BigDecimal.valueOf(-1)));
    }
}
