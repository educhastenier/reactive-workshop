package com.bonitasoft.reactiveworkshop.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;

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
        artistAPI.findById("a5c59c0315098aaaaaaa0f7fb9b1");
    }
}