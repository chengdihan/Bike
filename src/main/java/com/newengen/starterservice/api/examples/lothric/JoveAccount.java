package com.newengen.starterservice.api.examples.lothric;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoveAccount {

    long    id;
    String  name;
    Instant updated_at;
}
