package com.bonitasoft.reactiveworkshop.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComment;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments;
import com.bonitasoft.reactiveworkshop.domain.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author Emmanuel Duchastenier
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtistAPITest {

    @Autowired
    private ArtistAPI artistAPI;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findById_should_return_artist_details_if_existing() throws Exception {
        // when:
        final Artist artist = artistAPI.findById("a5c59c0315098e6d67bb57610f7fb9b1");

        // then:
        assertThat(artist.getName()).isEqualTo("Adele");
    }

    @Test
    public void findById_should_throw_NotFoundException_for_unknown_artist() throws Exception {
        // then:
        expectedException.expect(NotFoundException.class);

        // when:
        artistAPI.findById("__q645qs88df354q354354dfg354sdf__"); // This id does not exist
    }

    @Test
    public void findLast10ArtistComments_should_return_the_first_10_comments_of_the_right_artist() throws Exception {
        // when:
        final ArtistWithComments details = artistAPI.findLast10ArtistComments("a5c59c0315098e6d67bb57610f7fb9b1");

        // then:
        assertThat(details.getArtistName()).isEqualTo("Adele");
        assertThat(details.getGenre()).isEqualTo("Pop");
        final List<Comment> comments = details.getComments();
        comments.forEach(c -> {
            System.out.println(format("%s: '%s'", c.getUserName(), c.getComment()));
            assertThat(c.getUserName()).isNotEmpty();
            assertThat(c.getComment()).isNotEmpty();
        });
    }

    @Test
    public void getStreamOfCommentAndArtistByGenre_should_return_the_proper_results_for_specific_genre() throws Exception {
        // when:
        WebTestClient client = WebTestClient.bindToController(artistAPI).build();
        FluxExchangeResult<ArtistWithComment> result = client.get().uri("/genre/{genre}/comments/stream", "Pop")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ArtistWithComment.class);
        Flux<ArtistWithComment> commentFlux = result.getResponseBody();

        // arbitrarily test the first 2 results only:
        StepVerifier.create(commentFlux)
                .consumeNextWith(c -> {
                    assertThat(c.getUserName()).isNotEmpty();
                    assertThat(c.getComment()).isNotEmpty();
                    assertThat(c.getGenre()).isEqualTo("Pop");
                })
                .consumeNextWith(c -> {
                    assertThat(c.getUserName()).isNotEmpty();
                    assertThat(c.getComment()).isNotEmpty();
                    assertThat(c.getGenre()).isEqualTo("Pop");
                })
                .thenCancel()
                .verify();
    }
}