package tdd.triangulo;

public class Triangulo {

    public TipoTriangulo classificar(int a, int b, int c) {
        if (!formaTriangulo(a, b, c)) {
            return TipoTriangulo.INVALIDO;
        }
        if (a == b && b == c) {
            return TipoTriangulo.EQUILATERO;
        }
        if (a == b || a == c || b == c) {
            return TipoTriangulo.ISOSCELES;
        }
        return TipoTriangulo.ESCALENO;
    }

    private boolean formaTriangulo(int a, int b, int c) {
        boolean todosPositivos = a > 0 && b > 0 && c > 0;
        // long evita estouro de int ao somar lados muito grandes
        return todosPositivos
            && (long) a + b > c
            && (long) a + c > b
            && (long) b + c > a;
    }
}
