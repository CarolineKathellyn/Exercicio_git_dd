package tdd.pessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonDAO {

    public static final String ERRO_NOME_PARTES = "O nome deve ter ao menos 2 partes";
    public static final String ERRO_NOME_LETRAS = "O nome deve ser composto apenas de letras";
    public static final String ERRO_IDADE = "A idade deve estar no intervalo [1, 200]";
    public static final String ERRO_SEM_EMAIL = "A pessoa deve ter pelo menos um email";
    public static final String ERRO_EMAIL_FORMATO = "Email com formato inválido: ";

    private static final int IDADE_MINIMA = 1;
    private static final int IDADE_MAXIMA = 200;
    private static final Pattern SOMENTE_LETRAS = Pattern.compile("\\p{L}+");
    // "_____@____._____": cada parte com ao menos um caractere
    private static final Pattern FORMATO_EMAIL = Pattern.compile("[^@\\s]+@[^@\\s.]+\\.[^@\\s.]+");

    private final List<Person> pessoas = new ArrayList<>();

    public void save(Person p) {
        List<String> erros = isValidToInclude(p);
        if (!erros.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", erros));
        }
        pessoas.add(p);
    }

    public List<String> isValidToInclude(Person p) {
        List<String> erros = new ArrayList<>(validarNome(p.name()));
        if (p.age() < IDADE_MINIMA || p.age() > IDADE_MAXIMA) {
            erros.add(ERRO_IDADE);
        }
        erros.addAll(validarEmails(p.emails()));
        return erros;
    }

    public List<Person> listar() {
        return List.copyOf(pessoas);
    }

    private static List<String> validarNome(String nome) {
        String[] partes = nome == null || nome.isBlank() ? new String[0] : nome.trim().split("\\s+");
        List<String> erros = new ArrayList<>();
        if (partes.length < 2) {
            erros.add(ERRO_NOME_PARTES);
        }
        for (String parte : partes) {
            if (!SOMENTE_LETRAS.matcher(parte).matches()) {
                erros.add(ERRO_NOME_LETRAS);
                break;
            }
        }
        return erros;
    }

    private static List<String> validarEmails(List<Email> emails) {
        if (emails == null || emails.isEmpty()) {
            return List.of(ERRO_SEM_EMAIL);
        }
        List<String> erros = new ArrayList<>();
        for (Email email : emails) {
            String nome = email.name();
            if (nome == null || !FORMATO_EMAIL.matcher(nome).matches()) {
                erros.add(ERRO_EMAIL_FORMATO + nome);
            }
        }
        return erros;
    }
}
