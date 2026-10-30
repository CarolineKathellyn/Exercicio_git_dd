package tdd.salario;

import java.math.BigDecimal;

public record Funcionario(String nome, String email, BigDecimal salarioBase, Cargo cargo) {
}
