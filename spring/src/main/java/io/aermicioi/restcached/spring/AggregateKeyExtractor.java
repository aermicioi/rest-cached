package io.aermicioi.restcached.spring;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Runs multiple key extracting strategies, and aggregates them into a single key collection.
 */
public class AggregateKeyExtractor implements KeyExtractor {

    @NotNull
    private final List<KeyExtractor> extractors;

    /**
     * Construct aggragete key extractor with following strategies
     * @param extractors extractors used to extract information from join point
     */
    public AggregateKeyExtractor(@NotNull List<KeyExtractor> extractors) {
        this.extractors = extractors;
    }

    @NotNull
    @Override
    public List<Object> extractKeys(@NotNull ProceedingJoinPoint point) {
        return this.extractors.stream()
                       .map(extractor -> extractor.extractKeys(point))
                       .flatMap(Collection::stream)
                       .collect(ImmutableList.toImmutableList());
    }
}
