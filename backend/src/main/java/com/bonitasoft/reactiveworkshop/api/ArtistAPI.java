package com.bonitasoft.reactiveworkshop.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;

@RestController
public class ArtistAPI {

    private ArtistRepository artistRepository;
    private RestTemplate commentService;

    public ArtistAPI(ArtistRepository artistRepository, RestTemplate commentService) {
        this.artistRepository = artistRepository;
        this.commentService = commentService;
    }

    @GetMapping("/artist/{id}")
    public Artist findById(@PathVariable String id) throws NotFoundException {
        return artistRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/artists")
    public List<Artist> findAll() throws NotFoundException {
        return artistRepository.findAll();
    }

    @GetMapping("artist/{artistId}/comments")
    public ArtistWithComments findLast10ArtistComments(@PathVariable String artistId) throws NotFoundException {
        final Artist artist = artistRepository.findById(artistId).orElseThrow(NotFoundException::new);
        final ResponseEntity<List> commentsForArtist = commentService.getForEntity("/comments/{artistId}/last10", List.class, artistId);
        final List<Comment> comments = ((List<Map>) commentsForArtist.getBody()).stream()
                .map(o -> Comment.builder()
                        .artist(artist.getName())
                        .comment(String.valueOf(o.get("comment")))
                        .userName(String.valueOf(o.get("userName")))
                        .build())
                .collect(Collectors.toList());
        return ArtistWithComments.builder().artistId(artist.getId()).artistName(artist.getName()).genre(artist.getGenre()).comments(comments).build();
    }
}
