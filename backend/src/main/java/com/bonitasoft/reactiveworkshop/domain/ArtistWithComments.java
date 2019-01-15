package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Emmanuel Duchastenier
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistWithComments {

    private String artistId;
    private String artistName;
    private String genre;
    List<Comment> comments;
}
