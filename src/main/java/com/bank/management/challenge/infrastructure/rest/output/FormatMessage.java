package com.bank.management.challenge.infrastructure.rest.output;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormatMessage implements Serializable {

    /**
     * Status or error code of message
     */
    String code;

    /**
     * Detail information of message
     */
    String detail;

}
