package tdd.pessoa;

import java.util.List;

public record Person(int id, String name, int age, List<Email> emails) {
}
