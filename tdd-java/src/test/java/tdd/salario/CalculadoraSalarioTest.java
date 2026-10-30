package tdd.salario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CalculadoraSalarioTest {

    private CalculadoraSalario calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraSalario();
    }

    private static Funcionario funcionario(Cargo cargo, String salarioBase) {
        return new Funcionario("Maria Silva", "maria@exemplo.com", new BigDecimal(salarioBase), cargo);
    }

    // Percentual de desconto por cargo e faixa salarial

    @ParameterizedTest
    @CsvSource({
        "DESENVOLVEDOR, 3000.00, 0.20",
        "DESENVOLVEDOR, 8000.00, 0.20",
        "DESENVOLVEDOR, 2999.99, 0.10",
        "DESENVOLVEDOR, 1500.00, 0.10",
        "DBA, 2000.00, 0.25",
        "DBA, 6000.00, 0.25",
        "DBA, 1999.99, 0.15",
        "DBA, 1000.00, 0.15",
        "TESTADOR, 2000.00, 0.25",
        "TESTADOR, 4000.00, 0.25",
        "TESTADOR, 1999.99, 0.15",
        "TESTADOR, 1000.00, 0.15",
        "GERENTE, 5000.00, 0.30",
        "GERENTE, 12000.00, 0.30",
        "GERENTE, 4999.99, 0.20",
        "GERENTE, 3000.00, 0.20"
    })
    void percentualDeDescontoPorCargoEFaixaSalarial(Cargo cargo, String salario, String percentual) {
        assertEquals(new BigDecimal(percentual), calculadora.percentualDesconto(funcionario(cargo, salario)));
    }

    // Salário líquido

    @ParameterizedTest
    @CsvSource({
        "DESENVOLVEDOR, 3000.00, 2400.00",
        "DESENVOLVEDOR, 2000.00, 1800.00",
        "DBA, 2000.00, 1500.00",
        "DBA, 1000.00, 850.00",
        "TESTADOR, 2000.00, 1500.00",
        "TESTADOR, 1000.00, 850.00",
        "GERENTE, 5000.00, 3500.00",
        "GERENTE, 4000.00, 3200.00"
    })
    void salarioLiquidoPorCargo(Cargo cargo, String salario, String liquido) {
        assertEquals(
            new BigDecimal(liquido), calculadora.calcularSalarioLiquido(funcionario(cargo, salario)));
    }

    @ParameterizedTest
    @CsvSource({
        "DESENVOLVEDOR, 2999.99, 2699.99",
        "DBA, 1999.99, 1699.99",
        "TESTADOR, 1999.99, 1699.99",
        "GERENTE, 4999.99, 3999.99"
    })
    void salarioLiquidoLogoAbaixoDoLimiteUsaODescontoMenor(Cargo cargo, String salario, String liquido) {
        assertEquals(
            new BigDecimal(liquido), calculadora.calcularSalarioLiquido(funcionario(cargo, salario)));
    }

    @Test
    void salarioLiquidoEArredondadoParaCentavosComMeioParaCima() {
        // 1000.05 - 10% = 900.045
        BigDecimal resultado =
            calculadora.calcularSalarioLiquido(funcionario(Cargo.DESENVOLVEDOR, "1000.05"));

        assertEquals(new BigDecimal("900.05"), resultado);
    }

    @Test
    void salarioBaseZeroResultaEmLiquidoZero() {
        assertEquals(
            new BigDecimal("0.00"), calculadora.calcularSalarioLiquido(funcionario(Cargo.DBA, "0")));
    }

    @Test
    void salarioBaseSemCasasDecimaisTambemFunciona() {
        assertEquals(
            new BigDecimal("2400.00"),
            calculadora.calcularSalarioLiquido(funcionario(Cargo.DESENVOLVEDOR, "3000")));
    }

    // Validação do funcionário

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-0.01"})
    void salarioBaseNegativoLancaExcecao(String salario) {
        assertThrows(IllegalArgumentException.class, () -> funcionario(Cargo.DESENVOLVEDOR, salario));
    }

    @Test
    void salarioBaseNuloLancaExcecao() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Funcionario("Maria Silva", "maria@exemplo.com", null, Cargo.DBA));
    }

    @Test
    void cargoNuloLancaExcecao() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Funcionario("Maria Silva", "maria@exemplo.com", new BigDecimal("3000"), null));
    }
}
