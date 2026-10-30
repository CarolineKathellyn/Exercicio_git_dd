package tdd.salario;

import java.math.BigDecimal;

public record Funcionario(String nome, String email, BigDecimal salarioBase, Cargo cargo) {

    public Funcionario {
        if (cargo == null) {
            throw new IllegalArgumentException("O cargo é obrigatório");
        }
        if (salarioBase == null || salarioBase.signum() < 0) {
            throw new IllegalArgumentException("O salário base deve ser um valor não negativo");
        }
    }
}
