package com.yxy.beans;

import lombok.*;

import java.util.Set;

/**
 * Mail
 *
 * @author 余昕宇
 * @date 2018-06-21 1:33
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String subject;

    private String message;

    private Set<String> receivers;

}
