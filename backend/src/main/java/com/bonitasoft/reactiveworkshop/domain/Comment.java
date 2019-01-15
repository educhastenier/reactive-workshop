package com.bonitasoft.reactiveworkshop.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Comment {

    @NonNull
    String artist;
    @NonNull
    String userName;
    @NonNull
    String comment;

}