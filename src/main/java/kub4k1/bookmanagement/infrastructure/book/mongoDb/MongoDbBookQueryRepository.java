package kub4k1.bookmanagement.infrastructure.book.mongoDb;

import kub4k1.bookmanagement.domain.book.dto.BookDto;
import kub4k1.bookmanagement.domain.book.query.BookQueryRepository;
import kub4k1.bookmanagement.infrastructure.configuration.mongoDb.MongoDbQueryAndUpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class MongoDbBookQueryRepository implements BookQueryRepository {

    private final MongoTemplate mongoTemplate;
    private final BookConverter bookConverter = new BookConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public List<BookDto> findAllActiveBooksByUserId(int page, int elementPerPage, String userId) {
        Pageable pageable = PageRequest.of(page, elementPerPage);

        return mongoTemplate.find(mongoDbQueryAndUpdateUtil.userIdAndBookStatusIsActivePageableQuery(userId, pageable),
                BookDocument.class)
                .stream()
                .map(bookConverter::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<BookDto> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, BookDocument.class))
                .map(bookConverter::toDto);
    }
}
