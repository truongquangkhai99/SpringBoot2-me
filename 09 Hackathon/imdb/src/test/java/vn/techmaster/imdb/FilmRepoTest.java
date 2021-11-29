package vn.techmaster.imdb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.imdb.model.Film;
import vn.techmaster.imdb.repository.FilmRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class FilmRepoTest {
    @Autowired
    private FilmRepository filmRepo;

    @Test
    @DisplayName("getAll: Lấy tất cả phim")
    public void getAll() {
        List<Film> filmList = filmRepo.getAll();
        filmList.forEach(System.out::println);
        assertThat(filmList.size()).isEqualTo(30);
    }

    @Test
    @DisplayName("getFilmByCountry: Lấy phim theo quốc gia")
    public void getFilmByCountry() {
        var countryFilms = filmRepo.getFilmByCountry();
        List<Film> films = filmRepo.getAll();
        List<String> countries = films.stream().map(Film::getCountry)
                .distinct()
                .peek(System.out::println)
                .toList();

        countryFilms.forEach((s, films1) -> System.out.println(s + films1.stream()
                .toList()));

        assertThat(countries).containsAll(countryFilms.keySet());
    }

    @Test
    @DisplayName("getAllGeneres: Danh sách thể loại phim")
    public void getAllGeneres() {
        var generes = filmRepo.getAllGeneres();
        var films = filmRepo.getAll().stream()
                .map(Film::getGeneres).toList()
                .stream().flatMap(List::stream)
                .toList()
                .stream().peek(System.out::println);

        assertThat(films).containsAll(generes);
    }

//    @Test
//    @DisplayName("categorizeFilmByGenere: Phân loại phim theo thể loại")
//    public void categorizeFilmByGenere() {
//        var generesFilms = filmRepo.categorizeFilmByGenere();
//        var generes = filmRepo.getAll()
//                .stream().map(Film::getGeneres)
//                .flatMap(List::stream)
//                .distinct()
//                .peek(System.out::println)
//                .toList();
//
//        generesFilms.forEach(((s, films) -> System.out.println()));
//        assertThat(generes).containsAll(generesFilms.keySet());
//    }

    @Test
    @DisplayName("top5HighMarginFilms: Top 5 phim lãi nhất")
    public void top5HighMarginFilms() {
        var result = filmRepo.top5HighMarginFilms();
        result.forEach(System.out::println);
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Top 5 film có rating cao nhất nhưng lãi thì thấp nhất")
    public void top5FilmsHighRatingButLowMargin() {
        var result = filmRepo.top5FilmsHighRatingButLowMargin().stream().toList();
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Top 5 film từ năm 1990 đến 2000 có lãi lớn nhất")
    public void top5HighMarginFilmsIn1990to2000() {
        var result = filmRepo.top5HighMarginFilmsIn1990to2000().stream().toList();
        assertThat(result).hasSize(5);
    }

    @Test
    public void getcountryMakeMostFilms() {
        var result = filmRepo.getcountryMakeMostFilms();
        System.out.println(result);
        assertThat(result.get().getValue()).isEqualTo(9);

    }

    @Test
    public void yearMakeMostFilms() {
        var result = filmRepo.yearMakeMostFilms();
        System.out.println(result);
        assertThat(result.get().getValue()).isEqualTo(4);

    }

    @Test
    public void getFilmsMadeByCountryFromYearToYear() {
        var result = filmRepo.getFilmsMadeByCountryFromYearToYear("China", 1990, 1991);
        System.out.println(result);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void categorizeFilmByGenere(){
        filmRepo.categorizeFilmByGenere();

    }





}
