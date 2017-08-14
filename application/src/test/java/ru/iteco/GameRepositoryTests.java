package ru.iteco;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteco.game.GameCategoryRepository;


@RunWith(SpringRunner.class)
@TestPropertySource(
        locations= "classpath:application-test.properties")
@DataJpaTest
public class GameRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;
}
