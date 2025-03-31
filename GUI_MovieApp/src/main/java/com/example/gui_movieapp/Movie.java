package com.example.gui_movieapp;

public class Movie {
    private Long id;
    private String title;
    private String genre;
    private String director;
    private int releaseYear;
    private String actors;
    private String description;

    public Movie() {}

    public Movie(Long id,String title, String genre, String director, int releaseYear, String actors,String description) {
      this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.releaseYear = releaseYear;
        this.actors = actors;
        this.description = description;
    }
    public Movie(String title, String genre, String director, int releaseYear, String actors,String description) {

        this.title = title;
        this.genre = genre;
        this.director = director;
        this.releaseYear = releaseYear;
        this.actors = actors;
        this.description = description;
    }
    public Movie(String title, String genre, String director, int releaseYear, String actors) {

        this.title = title;
        this.genre = genre;
        this.director = director;
        this.releaseYear = releaseYear;
        this.actors = actors;
    }
    public String getActorsAsString() {
        return String.join(", ", actors);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors = actors; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format(" title=%s, genre=%s, director=%s, releaseYear=%d, actors=%s description=%s",
                title, genre, director, releaseYear, actors, description);
    }
}
