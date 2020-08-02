package com.arun.restservicewithwebclient.model;

import lombok.*;

/**
 * @author arun on 8/2/20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private Long id;
    private String uuid;
    private String email;
    private String tokenId;
    private String first_name;
    private String last_name;
    private String url;
}
