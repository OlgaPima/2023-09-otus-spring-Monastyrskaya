package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Errors;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrmBookCommentRepository implements BookCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(entityManager.find(BookComment.class, id));
    }

    @Override
    public BookComment insert(BookComment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public BookComment update(BookComment comment) {
        entityManager.merge(comment);
        return comment;
    }

    @Override
    public void delete(long commentId) {
        findById(commentId).ifPresentOrElse(
                comment -> entityManager.remove(comment),
                // Выбрасываем EntityNotFoundException, если не найдено такой записи в БД:
                () -> { throw new EntityNotFoundException(Errors.COMMENT_NOT_FOUND.getMessage().formatted(commentId)); }
        );
    }
}
