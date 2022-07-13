package com.bank.management.challenge.infrastructure.rest.output;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormatOutput<T> {

    /**
     * Data object of response
     */
    T data;

    /**
     * Messages list of response
     */
    List<FormatMessage> messages;

}
