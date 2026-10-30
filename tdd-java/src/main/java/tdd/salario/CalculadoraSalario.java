package tdd.salario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CalculadoraSalario {

    /** Regra de um cargo: a partir do limite vale o desconto maior, abaixo dele o menor. */
    private record Regra(BigDecimal limite, BigDecimal descontoMaior, BigDecimal descontoMenor) {
        BigDecimal descontoPara(BigDecimal salario) {
            return salario.compareTo(limite) >= 0 ? descontoMaior : descontoMenor;
        }
    }

    private static final Map<Cargo, Regra> REGRAS =
        Map.of(
            Cargo.DESENVOLVEDOR, regra("3000", "0.20", "0.10"),
            Cargo.DBA, regra("2000", "0.25", "0.15"),
            Cargo.TESTADOR, regra("2000", "0.25", "0.15"),
            Cargo.GERENTE, regra("5000", "0.30", "0.20"));

    private static Regra regra(String limite, String descontoMaior, String descontoMenor) {
        return new Regra(new BigDecimal(limite), new BigDecimal(descontoMaior), new BigDecimal(descontoMenor));
    }

    public BigDecimal percentualDesconto(Funcionario funcionario) {
        return REGRAS.get(funcionario.cargo()).descontoPara(funcionario.salarioBase());
    }

    public BigDecimal calcularSalarioLiquido(Funcionario funcionario) {
        BigDecimal fator = BigDecimal.ONE.subtract(percentualDesconto(funcionario));
        return funcionario.salarioBase().multiply(fator).setScale(2, RoundingMode.HALF_UP);
    }
}
