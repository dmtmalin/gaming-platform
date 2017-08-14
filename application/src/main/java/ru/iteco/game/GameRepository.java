package ru.iteco.game;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findOneByName(String name);

    @EntityGraph(value = "Game.withCurrentGameVersionAndUser", type = EntityGraph.EntityGraphType.LOAD)
    Collection<Game> findAllByGameCategoryFkAndIsPublishedIsTrueAndCurrentGameVersionIsNotNull(Integer gameCategoryFk);

    @EntityGraph(value = "Game.withCurrentGameVersion", type = EntityGraph.EntityGraphType.LOAD)
    Collection<Game> findAllByUserFkOrderByPriority(Integer userFk);
}
