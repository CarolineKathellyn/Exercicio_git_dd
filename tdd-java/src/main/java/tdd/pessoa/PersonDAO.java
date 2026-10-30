package tdd.pessoa;

import java.util.List;

public class PersonDAO {

    public static final String ERRO_NOME_PARTES = "O nome deve ter ao menos 2 partes";
    public static final String ERRO_NOME_LETRAS = "O nome deve ser composto apenas de letras";
    public static final String ERRO_IDADE = "A idade deve estar no intervalo [1, 200]";
    public static final String ERRO_SEM_EMAIL = "A pessoa deve ter pelo menos um email";
    public static final String ERRO_EMAIL_FORMATO = "Email com formato inválido: ";

    public void save(Person p) {
        throw new UnsupportedOperationException("não implementado");
    }

    public List<String> isValidToInclude(Person p) {
        throw new UnsupportedOperationException("não implementado");
    }

    public List<Person> listar() {
        throw new UnsupportedOperationException("não implementado");
    }
}
