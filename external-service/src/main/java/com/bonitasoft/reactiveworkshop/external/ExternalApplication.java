package com.bonitasoft.reactiveworkshop.external;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@SpringBootApplication
@RestController
public class ExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalApplication.class, args);
    }

    private static final long REPEAT_NUMBER_FOR_10_COMMENTS = 9L;

    private CommentGenerator commentGenerator;

    public ExternalApplication(CommentGenerator commentGenerator) {
        this.commentGenerator = commentGenerator;
    }

    @GetMapping("/comments/{artistId}/last10")
    Flux<Comment> findLast10Comments(@PathVariable String artistId) {
        return commentGenerator.generateComment(artistId)
                .repeat(REPEAT_NUMBER_FOR_10_COMMENTS);

    }
    @GetMapping(path = "/comments/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<Comment> getStreamOfComment() {
        return commentGenerator.generateComment().repeat().delayElements(Duration.ofMillis(50));
    }
    @GetMapping("/comments/last10")
    Flux<Comment> findLast10Comments() {
        return commentGenerator.generateComment().repeat(REPEAT_NUMBER_FOR_10_COMMENTS);
    }

    @GetMapping(path = "/comments/{artistId}/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    Flux<Comment> getStreamOfComment(@PathVariable String artistId) {
        return commentGenerator.generateComment(artistId).repeat().delayElements(Duration.ofSeconds(1));
    }


}
