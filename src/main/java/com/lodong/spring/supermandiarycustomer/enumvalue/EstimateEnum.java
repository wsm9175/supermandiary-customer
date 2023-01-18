package com.lodong.spring.supermandiarycustomer.enumvalue;
// 시공사의 주문에 대한 가격 선정한 견적서
public enum EstimateEnum {
    // 시공사가 소비자가보낸 전자계약서에대해 견적서를 작성하고 전송했을때
    SEND("전송"),
    PROCESSED("처리완료"),
    REJECT("반려");

    private final String label;
    EstimateEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
