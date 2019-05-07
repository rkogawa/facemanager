package facetec.core.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by rkogawa on 05/05/19.
 */
public class Teste {

    public static void main(String[] args) {
        LocalDateTime of = LocalDateTime.of(2019, 5, 5, 0, 44);
        System.out.println(of);
        Long ofMs = Timestamp.valueOf(of).getTime();
        System.out.println(ofMs);
        System.out.println(new Timestamp(ofMs));

        System.out.println(new BCryptPasswordEncoder(11).encode("facetec"));
    }

}
