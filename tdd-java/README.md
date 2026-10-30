# TDD em Java

Versão em **Java 17** com **JUnit 5** e **JaCoCo** de três exercícios de TDD, feita como prática. Cada exercício foi feito em ciclos de TDD, e o histórico do Git mostra os ciclos: um commit `test(...) (red)` com os testes falhando, seguido de um commit `feat(...) (green)` com a implementação que os faz passar.

## Estrutura

```
src/main/java/tdd/
├── triangulo/   # Exercício 1: Triangulo, TipoTriangulo
├── pessoa/      # Exercício 2: Person, Email, PersonDAO
└── salario/     # Exercício 3: Cargo, Funcionario, CalculadoraSalario
src/test/java/tdd/
├── triangulo/TrianguloTest.java
├── pessoa/PersonDAOTest.java
└── salario/CalculadoraSalarioTest.java
```

## Build

Requisitos: JDK 17 ou superior e Maven 3.9 ou superior.

```bash
cd tdd-java
mvn compile
```

## Execução dos testes e cobertura

```bash
mvn test                        # roda os testes e gera o relatório de cobertura
mvn test -Dtest=TrianguloTest   # um exercício
```

O JaCoCo gera o relatório em `target/site/jacoco/index.html` (e em `jacoco.csv` e `jacoco.xml` na mesma pasta).

## Exercício 1: Triângulo

`Triangulo.classificar(a, b, c)` devolve um `TipoTriangulo`: `EQUILATERO`, `ISOSCELES`, `ESCALENO` ou `INVALIDO`.

| Situação | Entradas | Resultado |
| --- | --- | --- |
| Escaleno válido | (3, 4, 5) | ESCALENO |
| Isósceles válido | (5, 5, 3) | ISOSCELES |
| Equilátero válido | (3, 3, 3) | EQUILATERO |
| Isósceles, permutações dos mesmos valores | (5, 5, 3), (5, 3, 5), (3, 5, 5) | ISOSCELES |
| Um valor zero | (0, 4, 5), (4, 0, 5), (4, 5, 0) | INVALIDO |
| Um valor negativo | (-3, 4, 5), (3, -4, 5), (3, 4, -5) | INVALIDO |
| Soma de 2 lados igual ao terceiro, as 6 permutações | de (1, 2, 3) | INVALIDO |
| Soma de 2 lados menor que o terceiro, as 6 permutações | de (1, 2, 4) | INVALIDO |
| Três valores zero | (0, 0, 0) | INVALIDO |
| Lados muito grandes (a soma estouraria o `int`) | (MAX_VALUE, MAX_VALUE, MAX_VALUE) | EQUILATERO |

## Exercício 2: Person, Email e PersonDAO

`PersonDAO.isValidToInclude(p)` devolve a lista de mensagens de erro, vazia se a pessoa é válida. As regras são:

- O nome tem ao menos 2 partes (separadas por espaço) e cada parte é composta só de letras, com acentos aceitos.
- A idade está no intervalo [1, 200], com os limites incluídos.
- A pessoa tem pelo menos um `Email`.
- O nome de cada `Email` segue `_____@____._____`: uma parte antes do `@` e duas partes separadas por ponto depois, cada uma com ao menos um caractere e sem espaços.

Todos os erros encontrados são acumulados na lista. `PersonDAO.save(p)` guarda a pessoa em memória, ou lança `IllegalArgumentException` com os erros e não guarda nada. `PersonDAO.listar()` devolve as pessoas guardadas.

## Exercício 3: Calculadora de salário

`CalculadoraSalario.calcularSalarioLiquido(funcionario)` desconta do salário base o percentual do cargo. O valor é um `BigDecimal` arredondado para centavos (meio para cima). `percentualDesconto(funcionario)` devolve o percentual aplicado.

| Cargo | Salário base | Desconto |
| --- | --- | --- |
| DESENVOLVEDOR | maior ou igual a 3.000,00 | 20% |
| DESENVOLVEDOR | menor que 3.000,00 | 10% |
| DBA | maior ou igual a 2.000,00 | 25% |
| DBA | menor que 2.000,00 | 15% |
| TESTADOR | maior ou igual a 2.000,00 | 25% |
| TESTADOR | menor que 2.000,00 | 15% |
| GERENTE | maior ou igual a 5.000,00 | 30% |
| GERENTE | menor que 5.000,00 | 20% |

`Funcionario` valida os dados ao ser criado: o cargo é obrigatório e o salário base não pode ser nulo nem negativo (`IllegalArgumentException`).

## Evidências de cobertura

Resultado de `mvn clean test`: 97 testes, todos passando. Cobertura medida pelo JaCoCo, por exercício:

| Exercício | Testes | Instruções | Branches | Linhas |
| --- | --- | --- | --- | --- |
| 1. Triângulo (`tdd.triangulo`) | 26 | 102 de 102 (100%) | 26 de 26 (100%) | 15 de 15 (100%) |
| 2. Pessoa (`tdd.pessoa`) | 36 | 183 de 183 (100%) | 26 de 26 (100%) | 35 de 35 (100%) |
| 3. Salário (`tdd.salario`) | 35 | 147 de 147 (100%) | 8 de 8 (100%) | 24 de 24 (100%) |
| **Total** | **97** | **432 de 432 (100%)** | **60 de 60 (100%)** | **74 de 74 (100%)** |
