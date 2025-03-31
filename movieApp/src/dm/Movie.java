package dm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String genre;
    private String director;
    private List<String> actors;
    private int releaseYear;
    private String description;

    public Movie(){

    }

    public Movie(String title, String genre, String director, List<String> actors, int releaseYear, String description) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.releaseYear = releaseYear;
        this.description = description;
    }
    @Override
    public String toString() {
        StringBuilder actorsString = new StringBuilder();
        return "Movie{" +
                "id=" + id +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", actors=" + actors +'\''+
                ", releaseYear=" + releaseYear +
                ", description='" + description + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDirector() { return director; }
    public void setTitle(String title) {this.title = title;}
    public void setDirector(String director) { this.director = director; }
    public List<String> getActors() {
        if (actors == null) {
            return new ArrayList<>();
        }
        return actors;
    }
    public String getActorsAsString() {
        return String.join(", ", actors); // ✅ ממיר את הרשימה למחרוזת
    }
    public void setActors(List<String> actors) {this.actors = actors;}
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
