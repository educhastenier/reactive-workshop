package com.bonitasoft.reactiveworkshop.domain;

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
public class ArtistWithComment {

    private String artistId;
    private String artistName;
    private String genre;
    private String userName;
    private String comment;
}
