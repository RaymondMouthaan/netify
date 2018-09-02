package org.mouthaan.netify.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * The class Movie Entity.
 * Constructors, getters and setters are generated by Lombok
 */
@Entity(name = "Movie")
@Table(name = "ntfy_movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "imdb_id")
    private String imdbId;

    @Column(name = "title")
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "runtime")
    private Integer runtime;

    @Column(name = "original_language")
    private String originalLanguage;

    @Column(name = "popularity")
    private Double popularity;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "budget")
    private Integer budget;

    @Column(name = "revenue")
    private Integer revenue;

    @Column(name = "status")
    private String status;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "overview")
    private String overview;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ntfy_movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Role> cast = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (getId() != null ? !getId().equals(movie.getId()) : movie.getId() != null) return false;
        if (getImdbId() != null ? !getImdbId().equals(movie.getImdbId()) : movie.getImdbId() != null) return false;
        if (getTitle() != null ? !getTitle().equals(movie.getTitle()) : movie.getTitle() != null) return false;
        if (getOriginalTitle() != null ? !getOriginalTitle().equals(movie.getOriginalTitle()) : movie.getOriginalTitle() != null)
            return false;
        if (getReleaseDate() != null ? !getReleaseDate().equals(movie.getReleaseDate()) : movie.getReleaseDate() != null)
            return false;
        if (getRuntime() != null ? !getRuntime().equals(movie.getRuntime()) : movie.getRuntime() != null) return false;
        if (getOriginalLanguage() != null ? !getOriginalLanguage().equals(movie.getOriginalLanguage()) : movie.getOriginalLanguage() != null)
            return false;
        if (getPopularity() != null ? !getPopularity().equals(movie.getPopularity()) : movie.getPopularity() != null)
            return false;
        if (getHomepage() != null ? !getHomepage().equals(movie.getHomepage()) : movie.getHomepage() != null)
            return false;
        if (getBudget() != null ? !getBudget().equals(movie.getBudget()) : movie.getBudget() != null) return false;
        if (getRevenue() != null ? !getRevenue().equals(movie.getRevenue()) : movie.getRevenue() != null) return false;
        if (getStatus() != null ? !getStatus().equals(movie.getStatus()) : movie.getStatus() != null) return false;
        if (getTagline() != null ? !getTagline().equals(movie.getTagline()) : movie.getTagline() != null) return false;
        if (getOverview() != null ? !getOverview().equals(movie.getOverview()) : movie.getOverview() != null)
            return false;
        return (getGenres() != null && getGenres().equals(movie.getGenres()));
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getImdbId() != null ? getImdbId().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getOriginalTitle() != null ? getOriginalTitle().hashCode() : 0);
        result = 31 * result + (getReleaseDate() != null ? getReleaseDate().hashCode() : 0);
        result = 31 * result + (getRuntime() != null ? getRuntime().hashCode() : 0);
        result = 31 * result + (getOriginalLanguage() != null ? getOriginalLanguage().hashCode() : 0);
        result = 31 * result + (getPopularity() != null ? getPopularity().hashCode() : 0);
        result = 31 * result + (getHomepage() != null ? getHomepage().hashCode() : 0);
        result = 31 * result + (getBudget() != null ? getBudget().hashCode() : 0);
        result = 31 * result + (getRevenue() != null ? getRevenue().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getTagline() != null ? getTagline().hashCode() : 0);
        result = 31 * result + (getOverview() != null ? getOverview().hashCode() : 0);
        return result;
    }
}
