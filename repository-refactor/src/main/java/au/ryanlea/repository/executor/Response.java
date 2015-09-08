package au.ryanlea.repository.executor;

import java.util.List;

public interface Response<T> {
    List<T> getResults();
    void setResults(List<T> results);
}
