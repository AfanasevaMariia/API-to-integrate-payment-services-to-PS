package mir.repositories;

import mir.models.ParsedMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class IMessageRepositoryImpl implements IMessageRepository {
    @Override
    public List<ParsedMessage> getByMti(String mti) {
        return null;
    }

    @Override
    public ParsedMessage getByTransactionNumber(String transactionNumber) {
        return null;
    }

    @Override
    public List<ParsedMessage> getInDateRange(LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public List<ParsedMessage> getByHex(String hex) {
        return null;
    }

    @Override
    public List<ParsedMessage> getByEdited(boolean edited) {
        return null;
    }

    @Override
    public void deleteByMti(String mti) {

    }

    @Override
    public void deleteByTransactionNumber(String transactionNumber) {

    }

    @Override
    public void deleteInDateRange(LocalDateTime start, LocalDateTime end) {

    }

    @Override
    public void deleteByHex(String hex) {

    }

    @Override
    public void deleteByEdited(boolean edited) {

    }

    @Override
    public List<ParsedMessage> findAll() {
        return null;
    }

    @Override
    public List<ParsedMessage> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ParsedMessage> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ParsedMessage> findAllById(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(ParsedMessage parsedMessage) {

    }

    @Override
    public void deleteAll(Iterable<? extends ParsedMessage> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends ParsedMessage> S save(S s) {
        return null;
    }

    @Override
    public <S extends ParsedMessage> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<ParsedMessage> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends ParsedMessage> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<ParsedMessage> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ParsedMessage getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends ParsedMessage> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ParsedMessage> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ParsedMessage> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ParsedMessage> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ParsedMessage> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ParsedMessage> boolean exists(Example<S> example) {
        return false;
    }
}
