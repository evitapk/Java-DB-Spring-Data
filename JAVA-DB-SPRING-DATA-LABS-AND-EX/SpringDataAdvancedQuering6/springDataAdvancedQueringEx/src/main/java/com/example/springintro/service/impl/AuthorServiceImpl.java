package com.example.springintro.service.impl;

import com.example.springintro.model.entity.Author;
import com.example.springintro.model.entity.Book;
import com.example.springintro.repository.AuthorRepository;
import com.example.springintro.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    public static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (authorRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .forEach(row -> {
                    String[] fullName = row.split("\\s+");
                    Author author = new Author(fullName[0], fullName[1]);

                    authorRepository.save(author);
                });
    }

    public Author getRandomAuthor() {
        long randomId = ThreadLocalRandom.current()
                .nextLong(1, authorRepository.count() + 1);
        return authorRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<String> getAllAuthorsOrderByCountOfTheirBooks() {
        return authorRepository.findAllByBooksSizeDesc().stream().map(author ->
                String.format("%s %s %d", author.getFirstName(), author.getLastName(),
                        author.getBooks().size())).collect(Collectors.toList());
    }

    @Override
    public List<String> findAuthorFirstNameEndsWithString(String endString) {
        return authorRepository.findAllByFirstNameEndingWith(endString).stream().map(author ->
                        String.format("%s %s", author.getFirstName(), author.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public String findAllAuthorsAndTheirTotalCopies() {
        List<Author> authors = this.authorRepository.findAll();
        Map<String, Integer> authorsBookCount = new HashMap<>();

        return authors.stream()
                .map(author -> {
                    int copiesCount = author.getBooks().stream().mapToInt(Book::getCopies).sum();
                    String authorInfo = String.format("%s %s %d", author.getFirstName(), author.getLastName(), copiesCount);
                    authorsBookCount.put(authorInfo, copiesCount);

                    return authorInfo;
                })
                .sorted((a1, a2) -> authorsBookCount.get(a2) - authorsBookCount.get(a1))
                .collect(Collectors.joining("\r\n"));
    }
}

