package es.codeurjc.backend.mapper;

public interface EntityMapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDto(List<E> entityList);
}