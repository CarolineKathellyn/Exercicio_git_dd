package tdd.pessoa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tdd.pessoa.PersonDAO.ERRO_EMAIL_FORMATO;
import static tdd.pessoa.PersonDAO.ERRO_IDADE;
import static tdd.pessoa.PersonDAO.ERRO_NOME_LETRAS;
import static tdd.pessoa.PersonDAO.ERRO_NOME_PARTES;
import static tdd.pessoa.PersonDAO.ERRO_SEM_EMAIL;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PersonDAOTest {

    private PersonDAO dao;

    @BeforeEach
    void setUp() {
        dao = new PersonDAO();
    }

    private static Person pessoa(String nome, int idade, List<Email> emails) {
        return new Person(1, nome, idade, emails);
    }

    private static Person pessoaComNome(String nome) {
        return pessoa(nome, 30, emailValido());
    }

    private static Person pessoaComIdade(int idade) {
        return pessoa("Maria Silva", idade, emailValido());
    }

    private static Person pessoaComEmails(List<Email> emails) {
        return pessoa("Maria Silva", 30, emails);
    }

    private static List<Email> emailValido() {
        return List.of(new Email(1, "maria@exemplo.com"));
    }

    @Test
    void pessoaValidaNaoTemErros() {
        assertEquals(List.of(), dao.isValidToInclude(pessoa("Maria Silva", 30, emailValido())));
    }

    // Nome

    @Test
    void nomeComApenasUmaParteEErro() {
        assertEquals(List.of(ERRO_NOME_PARTES), dao.isValidToInclude(pessoaComNome("Maria")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void nomeVazioOuAusenteEErro(String nome) {
        assertTrue(dao.isValidToInclude(pessoaComNome(nome)).contains(ERRO_NOME_PARTES));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Maria S1lva", "Maria 123", "Maria Silva!", "M@ria Silva"})
    void nomeComCaractereQueNaoELetraEErro(String nome) {
        assertEquals(List.of(ERRO_NOME_LETRAS), dao.isValidToInclude(pessoaComNome(nome)));
    }

    @Test
    void nomeComAcentosEMaisDeDuasPartesEValido() {
        assertEquals(List.of(), dao.isValidToInclude(pessoaComNome("José Ângelo da Conceição")));
    }

    @Test
    void espacosExtrasEntreAsPartesDoNomeSaoAceitos() {
        assertEquals(List.of(), dao.isValidToInclude(pessoaComNome("  Maria    Silva ")));
    }

    // Idade

    @ParameterizedTest
    @ValueSource(ints = {1, 200})
    void idadeNosLimitesDoIntervaloEValida(int idade) {
        assertEquals(List.of(), dao.isValidToInclude(pessoaComIdade(idade)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 201, 1000})
    void idadeForaDoIntervaloEErro(int idade) {
        assertEquals(List.of(ERRO_IDADE), dao.isValidToInclude(pessoaComIdade(idade)));
    }

    // Emails

    @Test
    void pessoaSemEmailEErro() {
        assertEquals(List.of(ERRO_SEM_EMAIL), dao.isValidToInclude(pessoaComEmails(List.of())));
    }

    @Test
    void listaDeEmailsNulaEErro() {
        assertEquals(List.of(ERRO_SEM_EMAIL), dao.isValidToInclude(pessoaComEmails(null)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@b.c", "maria.silva@exemplo.com", "maria_silva+tag@dominio.br"})
    void emailsNoFormatoCorretoSaoValidos(String email) {
        assertEquals(
            List.of(), dao.isValidToInclude(pessoaComEmails(List.of(new Email(1, email)))));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(
        strings = {
            "mariaexemplo.com",
            "maria@exemplocom",
            "@exemplo.com",
            "maria@.com",
            "maria@exemplo.",
            "maria@@exemplo.com",
            "ma ria@exemplo.com",
            "maria@exemplo.com."
        })
    void emailsForaDoFormatoSaoErro(String email) {
        assertEquals(
            List.of(ERRO_EMAIL_FORMATO + email),
            dao.isValidToInclude(pessoaComEmails(List.of(new Email(1, email)))));
    }

    @Test
    void apenasOEmailInvalidoGeraErroQuandoHaVarios() {
        List<Email> emails =
            List.of(
                new Email(1, "maria@exemplo.com"),
                new Email(2, "invalido"),
                new Email(3, "outro@site.org"));

        assertEquals(
            List.of(ERRO_EMAIL_FORMATO + "invalido"),
            dao.isValidToInclude(pessoaComEmails(emails)));
    }

    // Vários erros

    @Test
    void todosOsErrosSaoAcumulados() {
        List<String> erros = dao.isValidToInclude(pessoa("Maria", 0, List.of()));

        assertEquals(List.of(ERRO_NOME_PARTES, ERRO_IDADE, ERRO_SEM_EMAIL), erros);
    }

    // save

    @Test
    void saveGuardaAPessoaValida() {
        Person p = pessoa("Maria Silva", 30, emailValido());

        dao.save(p);

        assertEquals(List.of(p), dao.listar());
    }

    @Test
    void saveDePessoaInvalidaLancaExcecaoComOsErrosENaoGuarda() {
        IllegalArgumentException erro =
            assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(pessoa("Maria", 0, emailValido())));

        assertTrue(erro.getMessage().contains(ERRO_NOME_PARTES));
        assertTrue(erro.getMessage().contains(ERRO_IDADE));
        assertEquals(List.of(), dao.listar());
    }
}
