package ru.iteco;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteco.game.GameCategory;
import ru.iteco.game.GameCategoryRepository;


import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@TestPropertySource(
        locations= "classpath:application-test.properties")
@DataJpaTest
public class GameCategoryRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Test(expected = PersistenceException.class)
    public void createWithoutName() {
        GameCategory category = new GameCategory();
        entityManager.persist(category);
    }

    @Test(expected = PersistenceException.class)
    public void createWithoutPriority() {
        GameCategory category = new GameCategory();
        category.setName("test");
        entityManager.persist(category);
    }

    @Test
    public void createFull() {
        GameCategory category = new GameCategory();
        category.setName("test");
        category.setPriority(0);
        entityManager.persist(category);
        entityManager.flush();

        long count = gameCategoryRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void findAllByOrderByPriorityDesc() {
        GameCategory category = new GameCategory();
        category.setName("test1");
        category.setPriority(0);
        entityManager.persist(category);
        entityManager.flush();

        category = new GameCategory();
        category.setName("test2");
        category.setPriority(1);
        entityManager.persist(category);
        entityManager.flush();

        Collection<GameCategory> categories = gameCategoryRepository.findAllByOrderByPriorityDesc();
        Iterator<GameCategory> iterator = categories.iterator();

        assertThat(categories.size()).isEqualTo(2);

        category = iterator.next();

        assertThat(category.getName()).isEqualTo("test2");

        category = iterator.next();

        assertThat(category.getName()).isEqualTo("test1");
    }
}
