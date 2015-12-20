package pl.pogorzelski.webconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pogorzelski.webconverter.domain.Converter;

import java.util.List;
import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
@Repository
public interface ConverterRepository extends JpaRepository<Converter, Long> {

    List<Converter> findBySourceFormat(String sourceFormat);

    Optional<Converter> findOneBySourceFormatAndTargetFormat(String sourceFormat, String targetFormat);
}