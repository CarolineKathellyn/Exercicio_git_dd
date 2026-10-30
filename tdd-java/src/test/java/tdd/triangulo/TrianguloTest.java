package tdd.triangulo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class TrianguloTest {

    @Test
    void escalenoValido() {
        assertEquals(TipoTriangulo.ESCALENO, Triangulo.classificar(3, 4, 5));
    }

    @Test
    void isoscelesValido() {
        assertEquals(TipoTriangulo.ISOSCELES, Triangulo.classificar(5, 5, 3));
    }

    @Test
    void equilateroValido() {
        assertEquals(TipoTriangulo.EQUILATERO, Triangulo.classificar(3, 3, 3));
    }

    @ParameterizedTest
    @CsvSource({"5, 5, 3", "5, 3, 5", "3, 5, 5"})
    void isoscelesValidoComPermutacaoDosMesmosValores(int a, int b, int c) {
        assertEquals(TipoTriangulo.ISOSCELES, Triangulo.classificar(a, b, c));
    }

    @ParameterizedTest
    @CsvSource({"0, 4, 5", "4, 0, 5", "4, 5, 0"})
    void umValorZeroEInvalido(int a, int b, int c) {
        assertEquals(TipoTriangulo.INVALIDO, Triangulo.classificar(a, b, c));
    }

    @ParameterizedTest
    @CsvSource({"-3, 4, 5", "3, -4, 5", "3, 4, -5"})
    void umValorNegativoEInvalido(int a, int b, int c) {
        assertEquals(TipoTriangulo.INVALIDO, Triangulo.classificar(a, b, c));
    }

    @ParameterizedTest
    @MethodSource("permutacoesDe123")
    void somaDeDoisLadosIgualAoTerceiroEInvalido(int a, int b, int c) {
        assertEquals(TipoTriangulo.INVALIDO, Triangulo.classificar(a, b, c));
    }

    @ParameterizedTest
    @MethodSource("permutacoesDe124")
    void somaDeDoisLadosMenorQueOTerceiroEInvalido(int a, int b, int c) {
        assertEquals(TipoTriangulo.INVALIDO, Triangulo.classificar(a, b, c));
    }

    @Test
    void tresValoresZeroEInvalido() {
        assertEquals(TipoTriangulo.INVALIDO, Triangulo.classificar(0, 0, 0));
    }

    static Stream<Arguments> permutacoesDe123() {
        return permutacoes(1, 2, 3);
    }

    static Stream<Arguments> permutacoesDe124() {
        return permutacoes(1, 2, 4);
    }

    private static Stream<Arguments> permutacoes(int x, int y, int z) {
        return List.of(
                Arguments.of(x, y, z),
                Arguments.of(x, z, y),
                Arguments.of(y, x, z),
                Arguments.of(y, z, x),
                Arguments.of(z, x, y),
                Arguments.of(z, y, x))
            .stream();
    }
}
