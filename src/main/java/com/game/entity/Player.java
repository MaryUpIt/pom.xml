package com.game.entity;

import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "Id")
    private long id;
    @Column (name = "name")
    private String name;
    @Column (name = "title")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column (name = "race")
    private Race race;
    @Enumerated(EnumType.STRING)
    @Column (name = "profession")
    private Profession profession;
    @Column (name = "experience")
    private Integer experience;
    @Column (name = "level")
    private Integer level;
    @Column (name = "untilNextLevel")
    private Integer untilNextLevel;//Остаток опыта до следующего уровня
    @Column (name = "birthday")
    private Date birthday;// Дата регистрации
    @Column (name = "banned")
    private Boolean banned;

    public long getId() {
        return id;
    }

    public void setId(int playerId) {
        this.id=playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;

        return id == player.id
                && race == player.race
                && profession == player.profession
                && Objects.equals(name, player.name)
                && Objects.equals(title, player.title)
                && Objects.equals(experience, player.experience)
                && Objects.equals(level, player.level)
                && Objects.equals(untilNextLevel, player.untilNextLevel)
                && Objects.equals(birthday, player.birthday)
                && Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }

}
