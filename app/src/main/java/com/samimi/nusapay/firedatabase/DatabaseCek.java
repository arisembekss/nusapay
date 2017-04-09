package com.samimi.nusapay.firedatabase;

/**
 * Created by aris on 10/12/16.
 */

public class DatabaseCek {
    public String user;
    public String email;
    public String trx;
    public String tagihan;

    public DatabaseCek() {

    }
    public DatabaseCek(String user, String email, String trx, String tagihan) {
        this.user = user;
        this.email = email;
        this.trx = trx;
        this.tagihan = tagihan;
    }
}
