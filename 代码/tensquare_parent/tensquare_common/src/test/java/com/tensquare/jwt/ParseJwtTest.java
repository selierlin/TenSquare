package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseJwtTest {
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMTQ5NTI2MjgwMTE1NDU4MDQ4Iiwic3ViIjoieGl4aSIsImlhdCI6MTU2MzAwODkyOSwicm9sZXMiOiJ1c2VyIiwiZXhwIjoxNTYzMDA5Mjg5fQ.nxEbA0nxwMpOKWzjiG7NQ_MO39gWtlYB1MUfFW1OlaA";
        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("IssuedAt:" + claims.getIssuedAt());
    }
}
