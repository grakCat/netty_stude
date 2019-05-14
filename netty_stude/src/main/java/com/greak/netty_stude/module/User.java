package com.greak.netty_stude.module;

import com.greak.netty_stude.proto.ProtobufMessage;
import lombok.Builder;
import lombok.Data;

/**
 * Created on 2019/5/14.
 *
 * @author hy
 * @since 1.0
 */
@Data
@Builder
@ProtobufMessage
public class User {

    private String name;

    private String address;

    private int age;

    private int hight;
}
