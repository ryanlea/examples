package au.ryanlea.repository.executor;

public interface Executor<T> {
    Response<T> execute(final Request request);
}
