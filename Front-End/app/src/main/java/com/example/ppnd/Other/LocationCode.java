package com.example.ppnd.Other;

public class LocationCode {

    public static String current_address;
    public static int current_code;

    //위도 경도 위치 간결하게 포현
    public static String currentAddress(String Address) {
        if(Address.contains("서울"))
            current_address = "서울특별시";
        else if(Address.contains("인천"))
            current_address = "인천광역시";
        else if(Address.contains("경기도"))
            current_address = "경기도";
        else if(Address.contains("부산"))
            current_address = "부산광역시";
        else if(Address.contains("울산"))
            current_address = "울산광역시";
        else if(Address.contains("경상남도"))
            current_address = "경상남도";
        else if(Address.contains("대구"))
            current_address = "대구광역시";
        else if(Address.contains("경상북도"))
            current_address = "경상북도";
        else if(Address.contains("광주"))
            current_address = "광주광역시";
        else if(Address.contains("전라남도"))
            current_address = "전라남도";
        else if(Address.contains("전라북도"))
            current_address = "전라북도";
        else if(Address.contains("대전"))
            current_address = "대전광역시";
        else if(Address.contains("세종"))
            current_address = "세종시";
        else if(Address.contains("충청남도"))
            current_address = "충청남도";
        else if(Address.contains("충청북도"))
            current_address = "충청북도";
        else if(Address.contains("강원도"))
            current_address = "강원도";
        else if(Address.contains("제주도"))
            current_address = "제주도";

        return current_address;
    }

    public static int currentLocationCode(String Address) {
        if(Address.equals("서울특별시") || Address.equals("인천광역시") || Address.equals("경기도"))
            current_code = 109;
        else if(Address.equals("부산광역시") || Address.equals("울산광역시") || Address.equals("경상남도"))
            current_code = 159;
        else if(Address.equals("대구광역시") || Address.equals("경상북도"))
            current_code = 143;
        else if(Address.equals("광주광역시") || Address.equals("전라남도"))
            current_code = 156;
        else if(Address.equals("전라북도"))
            current_code = 146;
        else if(Address.equals("대전광역시") || Address.equals("세종시") || Address.equals("충청남도"))
            current_code = 133;
        else if(Address.equals("충청북도"))
            current_code = 131;
        else if(Address.equals("강원도"))
            current_code = 105;
        else if(Address.equals("제주도"))
            current_code = 184;

        return current_code;
    }
}