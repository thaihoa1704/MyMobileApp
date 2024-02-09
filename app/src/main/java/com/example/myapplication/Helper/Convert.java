package com.example.myapplication.Helper;

import java.text.NumberFormat;
import java.text.ParseException;

public class Convert {
    //Hàm đổi từ số sang chữ có định dạng
    public static String DinhDangTien(int tien) {
        return NumberFormat.getNumberInstance().format(tien);
    }

    //Hàm chuyển từ chữ sang số để tính toán
    public static int ChuyenTien(String tien) {
        try {
            return NumberFormat.getNumberInstance().parse(tien).intValue();
        } catch (ParseException ex) {

        }
        return 0;
    }


}
