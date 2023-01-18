package com.lodong.spring.supermandiarycustomer.enumvalue;
// 소비자의 주문목록이 담긴 전자 계약서
public enum RequestOrderEnum {
    // 소비자가 전자계약서를 전송했을때
    BASIC("일반"),
    // 시공사가 보류 상태로 바꿀수 있음
    DEFER("보류"),
    // 시공사가 삭제 상태로 바꿀수 있음
    DELETE("삭제"),
    // 시공사가 소비자가 보낸 전자계약서에대해 견적서를 작성하고 전송했을때
    PROCESSING("처리중"),
    // 시공사가 보낸 견적서를 소비자가 수락했을때
    PROCESSED("처리완료"),
    // 시공사가 보낸 견적서를 소비자가 반려시켰을때
    REJECT("반려");

    private final String label;
    RequestOrderEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
