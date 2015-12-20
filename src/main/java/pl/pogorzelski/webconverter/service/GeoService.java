package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.City;
import pl.pogorzelski.webconverter.domain.State;

import java.util.Set;

public interface GeoService {
    public Set<State> findAllStates();

    public Set<City> findCitiesForState(String state);

}
