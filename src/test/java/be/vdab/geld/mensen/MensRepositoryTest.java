package be.vdab.geld.mensen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Import(MensRepository.class)
@Sql("/mensen.sql")
public class MensRepositoryTest {
    private final MensRepository mensRepository;
    private final JdbcClient jdbcClient;
    private static final String MENSEN_TABLE = "mensen";
    public MensRepositoryTest(MensRepository mensRepository, JdbcClient jdbcClient) {
        this.mensRepository = mensRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    public void findAantalGeefHetJuisteAantalMensen() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, MENSEN_TABLE);
        assertThat(mensRepository.findAantal()).isEqualTo(aantalRecords);
    }

    @Test
    public void findAllGeeftAlleMensenGesorteerdOpId() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, MENSEN_TABLE);
        assertThat(mensRepository.findAll())
                .hasSize(aantalRecords)
                .extracting(Mens::getId)
                .isSorted();
    }

    @Test
    public void createVoegtEenMensToe() {
        var id = mensRepository.create(new Mens(0, "test3", BigDecimal.TEN));
        assertThat(id).isPositive();
        var aantalRecordsMetDeIdVanDeToegevoegdeMens =
                JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVanDeToegevoegdeMens).isOne();
    }

    private long idVanTestMens1() {
        return jdbcClient.sql("select id from mensen where naam = 'test1'")
                .query(Long.class)
                .single();
    }

    @Test
    public void deleteVerwijdertEenMens() {
        var id = idVanTestMens1();
        mensRepository.delete(id);
        var aantalRecordsMetDeIdVnDeVerwijderdeMens =
                JdbcTestUtils.countRowsInTableWhere(jdbcClient, MENSEN_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVnDeVerwijderdeMens).isZero();
    }

    @Test
    public void findByIdMetBestaandeIdVindtEenMens() {
        assertThat(mensRepository.findById(idVanTestMens1())).hasValueSatisfying(
                mens -> assertThat(mens.getNaam()).isEqualTo("test1")
        );
    }

    @Test
    public void findByIdMetOnbestaandeIdVindtGeenMens() {
        assertThat(mensRepository.findById(Long.MAX_VALUE)).isEmpty();
    }

    @Test
    public void updateWijzigtEenMens() {
        var id = idVanTestMens1();
        var mens = new Mens(id, "mens1", BigDecimal.TEN);

        mensRepository.update(mens);
        var aantalGewijzigdeRecords = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, MENSEN_TABLE, "id=" + id);
        assertThat(aantalGewijzigdeRecords).isOne();
    }

    @Test
    public void updateOnbestaandeMensMislukt() {
        assertThatExceptionOfType(MensNietGevondenException.class).isThrownBy(
                () -> mensRepository.update(new Mens(Long.MAX_VALUE, "test3", BigDecimal.TEN))
        );
    }

    @Test
    public void findByGeldBetweenVindtDeJuisteMensen() {
        var van = BigDecimal.ONE;
        var tot = BigDecimal.TEN;
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, MENSEN_TABLE, "geld between 1 and 10");
        assertThat(mensRepository.findByGeldBetween(van, tot))
                .hasSize(aantalRecords)
                .extracting(Mens::getGeld)
                .allSatisfy(geld -> assertThat(geld).isBetween(van, tot))
                .isSorted();
    }

    @Test
    public void findByIdsMetEenLegeLijstGeeftEenLegeLijstMensen() {
        assertThat(mensRepository.findByIds(List.of())).isEmpty();
    }

    private long idVanTestMens2() {
        return jdbcClient.sql("select id from mensen where naam = 'test2'")
                .query(Long.class)
                .single();
    }

    @Test
    public void findByIdsVindtDeJuisteMensen() {
        var id1 = idVanTestMens1();
        var id2 = idVanTestMens2();

        assertThat(mensRepository.findByIds(List.of(id1, id2)))
                .hasSize(2)
                .extracting(Mens::getId)
                .containsOnly(id1, id2);
    }
}
