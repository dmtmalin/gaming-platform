package ru.iteco.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GameVersionRepository extends JpaRepository<GameVersion, Integer> {
    GameVersion findFirstByGameOrderByBuildDesc(Game game);

    Collection<GameVersion> findAllByGameFkAndIsApproveIsTrueOrderByBuildDesc(Integer gameId);
}
