package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PlayerService {


    private PlayerRepository playerRepository;


    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private boolean invalidParameters(Player player) {
        if (player.getName().length() < 1 || player.getName().length() > 12) return true;

        if (player.getTitle().length() > 30) return true;

        if (player.getExperience() < 0 || player.getExperience() > 10_000_000) return true;

        if (player.getBirthday().getTime() < 0) return true;
        Calendar date = Calendar.getInstance();
        date.setTime(player.getBirthday());
        if (date.get(Calendar.YEAR) < 2_000 || date.get(Calendar.YEAR) > 3_000) return true;

        return false;
    }

    private void setLevelAndExpUntilNextLevel(Player player) {
        player.setLevel(calculateLevel(player));
        player.setUntilNextLevel(calculateExpUntilNextLevel(player));
    }

    private int calculateLevel(Player player) {
        int experience = player.getExperience();
        return (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
    }

    private int calculateExpUntilNextLevel(Player player) {
        int experience = player.getExperience();
        int level = calculateLevel(player);
        return 50 * (level + 1) * (level + 2) - experience;
    }

    public List<Player> getPlayersList(Specification<Player> specification) {
        return playerRepository.findAll(specification);
    }

    public Page<Player> getPlayersList(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }


    public Player createPlayer(Player requestPlayer) {
        if (requestPlayer == null
                || requestPlayer.getName() == null
                || requestPlayer.getTitle() == null
                || requestPlayer.getRace() == null
                || requestPlayer.getProfession() == null
                || requestPlayer.getBirthday() == null
                || requestPlayer.getExperience() == null) {
            return null;
        }

        if (invalidParameters(requestPlayer)) return null;

        if (requestPlayer.isBanned() == null) requestPlayer.setBanned(false);

        setLevelAndExpUntilNextLevel(requestPlayer);

        return playerRepository.saveAndFlush(requestPlayer);
    }

    public Player getPlayer(Long id) {
        if (playerRepository.findById(id).isPresent()) {
            return playerRepository.findById(id).get();
        }
        return null;
    }

    public Player updatePlayer(Long id, Player requestPlayer) {
        if (!playerRepository.findById(id).isPresent()) return null;

        Player responsePlayer = getPlayer(id);

        if (requestPlayer.getName() != null) responsePlayer.setName(requestPlayer.getName());
        if (requestPlayer.getTitle() != null) responsePlayer.setTitle(requestPlayer.getTitle());
        if (requestPlayer.getRace() != null) responsePlayer.setRace(requestPlayer.getRace());
        if (requestPlayer.getProfession() != null) responsePlayer.setProfession(requestPlayer.getProfession());
        if (requestPlayer.getBirthday() != null) responsePlayer.setBirthday(requestPlayer.getBirthday());
        if (requestPlayer.isBanned() != null) responsePlayer.setBanned(requestPlayer.isBanned());
        if (requestPlayer.getExperience() != null) responsePlayer.setExperience(requestPlayer.getExperience());

        setLevelAndExpUntilNextLevel(responsePlayer);
        return playerRepository.save(responsePlayer);
    }

    public boolean deletePlayer(Long id) {
        if (playerRepository.findById(id).isPresent()) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Specification<Player> nameFilter(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(root.get("name"), "%" + name + "%");
    }

    public Specification<Player> titleFilter(String title) {
        return (root, query, builder) -> title == null ? null : builder.like(root.get("title"), "%" + title + "%");
    }

    public Specification<Player> raceFilter(Race race) {
        return (root, query, builder) -> race == null ? null : builder.equal(root.get("race"), race);
    }

    public Specification<Player> professionFilter(Profession profession) {
        return (root, query, builder) -> profession == null ? null : builder.equal(root.get("profession"), profession);
    }

    public Specification<Player> experienceFilter(Integer min, Integer max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return builder.lessThanOrEqualTo(root.get("experience"), max);
            }
            if (max == null) {
                return builder.greaterThanOrEqualTo(root.get("experience"), min);
            }
            return builder.between(root.get("experience"), min, max);
        };
    }

    public Specification<Player> levelFilter(Integer min, Integer max) {
        return (root, query, builder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return builder.lessThanOrEqualTo(root.get("level"), max);
            }
            if (max == null) {
                return builder.greaterThanOrEqualTo(root.get("level"), min);
            }
            return builder.between(root.get("level"), min, max);
        };
    }

    public Specification<Player> birthdayFilter(Long after, Long before) {
        return (root, query, builder) -> {
            if (after == null && before == null) {
                return null;
            }
            if (after == null) {
                Date before1 = new Date(before);
                return builder.lessThanOrEqualTo(root.get("birthday"), before1);
            }
            if (before == null) {
                Date after1 = new Date(after);
                return builder.greaterThanOrEqualTo(root.get("birthday"), after1);
            }
            Date before1 = new Date(before - 3600001);
            Date after1 = new Date(after);
            return builder.between(root.get("birthday"), after1, before1);
        };
    }

    public Specification<Player> bannedFilter(Boolean isBanned) {
        return (root, query, builder) -> {
            if (isBanned == null) {
                return null;
            }
            if (isBanned) {
                return builder.isTrue(root.get("banned"));
            } else {
                return builder.isFalse(root.get("banned"));
            }
        };
    }

}