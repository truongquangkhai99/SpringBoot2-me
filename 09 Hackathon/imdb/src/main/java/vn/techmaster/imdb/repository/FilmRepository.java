package vn.techmaster.imdb.repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import vn.techmaster.imdb.model.Film;

@Repository
public class FilmRepository implements IFilmRepo {
    private List<Film> films;

    public FilmRepository(@Value("${datafile}") String datafile) {
        try {
            File file = ResourceUtils.getFile("classpath:static/" + datafile);
            ObjectMapper mapper = new ObjectMapper(); // Dùng để ánh xạ cột trong CSV với từng trường trong POJO
            films = Arrays.asList(mapper.readValue(file, Film[].class));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public List<Film> getAll() {
        return films;
    }

    @Override
    public Map<String, List<Film>> getFilmByCountry() {
        // TODO Auto-generated method stub
        return films.stream().collect(Collectors.groupingBy(Film::getCountry));
    }

    @Override
    public Optional<Entry<String, Long>> getcountryMakeMostFilms() {

        // TODO Auto-generated method stub

        return films.stream()
                .collect(
                        Collectors.groupingBy(Film::getCountry,
                                Collectors.counting())).entrySet()
                .stream().max(Entry.comparingByValue());
    }

    @Override
    public Optional<Entry<Integer, Long>> yearMakeMostFilms() {
        // TODO Auto-generated method stub

        return films.stream()
                .collect(
                        Collectors.groupingBy(Film::getYear,
                                Collectors.counting())).entrySet()
                .stream().max(Entry.comparingByValue());
    }


    @Override
    public List<Film> getFilmsMadeByCountryFromYearToYear(String country, int fromYear, int toYear) {
        // TODO Auto-generated method stub
        return films.stream()
                .filter(film -> film.getCountry().equals(country))
                .filter(film -> film.getYear() >= fromYear && film.getYear() <= toYear)
                .toList();

    }


    @Override
    public List<Film> top5HighMarginFilms() {
        // TODO Auto-generated method stub
        Comparator<Film> comparator
                = (f1, f2) -> f1.getRevenue() - f1.getCost() - (f2.getRevenue() - f2.getCost());
        comparator = comparator.reversed();
        films = films.stream()
                .sorted(comparator).limit(5).collect(Collectors.toList());
        return films;
    }

    @Override
    public List<Film> top5HighMarginFilmsIn1990to2000() {
        // TODO Auto-generated method stub
        Comparator<Film> comparator = (f1, f2) -> (f1.getRevenue() - f1.getCost() - (f2.getRevenue() - f2.getCost()));
        comparator = comparator.reversed();
        return films.stream()
                .filter(film -> film.getYear() >= 1990 && film.getYear() <= 2000)
                .sorted(comparator)
                .limit(5)
                .peek(System.out::println)
                .collect(Collectors.toList());

    }


    @Override
    public List<Film> top5FilmsHighRatingButLowMargin() {
        // TODO Auto-generated method stub
        return films.stream()
                .sorted(Comparator.comparing(Film::getRating).reversed()
                        .thenComparing((film -> film.getRevenue() - film.getCost())))
                .limit(5)
                .peek(System.out::println)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> getAllGeneres() {
        // TODO Auto-generated method stub
        var generesList = films.stream().map(Film::getGeneres).toList();
        return generesList.stream().flatMap(List::stream).distinct()
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, List<Film>> categorizeFilmByGenere() {
        var types = getAllGeneres();

        return types.stream()
                .map(unittype -> new ArrayList<Object>() {{
                            add(unittype);
                            add(films.stream()
                                    .filter(film -> film.getGeneres().contains(unittype))
                                    .collect(Collectors.toList()));
                        }}
                )
                .peek(System.out::println)
                .collect(Collectors.toMap(e -> (String) e.get(0), e -> (List<Film>) e.get(1)));
    }


    @Override
    public double ratioBetweenGenere(String genreX, String genreY) {
        // TODO Auto-generated method stub
        return 0;
    }

}
