package uz.tuit.unirules.projections;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface FuzzySearchProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getSource();
}
