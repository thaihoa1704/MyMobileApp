package com.example.myapplication.Helper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    //Hàm chuyển timestamp từ firestore sang ngày tháng năm
    public static String getDateTime(long timestamp){
        try {
            Date newDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sfd.format(newDate);

        } catch (Exception e) {
            return "date";
        }
    }
}
