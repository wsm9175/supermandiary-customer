package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.Exhibition;
import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionCustomerCommentDto;
import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionCustomerReplyDto;
import com.lodong.spring.supermandiarycustomer.dto.exhibition.*;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.ExhibitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/exhibition/customer")
public class ExhibitionController {
    private final JwtTokenProvider jwtTokenProvider;
    private final ExhibitionService exhibitionService;

    public ExhibitionController(JwtTokenProvider jwtTokenProvider, ExhibitionService exhibitionService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getExhibitionList(@RequestHeader(name = "Authorization") String token) {
        try {
            List<ExhibitionDTO> exhibitionDTOS = exhibitionService.getExhibitionDTOList(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "박람회 리스트", exhibitionDTOS);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getExhibitionBoardList(@RequestHeader(name = "Authorization") String token, String exhibitionId) {
        try {
            ExhibitionDetailDTO exhibitionDetailDTO = exhibitionService.getExhibitionBoardList(getMyUuId(token), exhibitionId);
            return getResponseMessage(StatusEnum.OK, "선택한 박람회 정보", exhibitionDetailDTO);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/board/detail")
    public ResponseEntity<?> getBoardDetail(@RequestHeader(name = "Authorization") String token, String boardId) {
        try {
            BoardDetailDTO boardDetailDTO = exhibitionService.getBoardDetail(getMyUuId(token), boardId);
            return getResponseMessage(StatusEnum.OK, "박람회 게시판 정보", boardDetailDTO);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/comment/group")
    public ResponseEntity<?> getCommentGroup(@RequestHeader(name = "Authorization") String token, String commentGroupId) {
        try {
            List<ExhibitionCommentDTO> exhibitionCommentDTOList = exhibitionService.getExhibitionCommentGroup(getMyUuId(token), commentGroupId);
            return getResponseMessage(StatusEnum.OK, "대댓글 목록", exhibitionCommentDTOList);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PostMapping("/board/comment")
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token, @RequestBody ExhibitionCustomerCommentDto exhibitionCustomerCommentDto) {
        try {
            List<ExhibitionCommentDTO> exhibitionCommentDTOList = exhibitionService.addComment(getMyUuId(token), exhibitionCustomerCommentDto.getBoardId(), exhibitionCustomerCommentDto.getComment());
            return getResponseMessage(StatusEnum.OK, "댓글 삽입 성공 업데이트 댓글 목록", exhibitionCommentDTOList);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());
        }
    }

    @PostMapping("/board/reply")
    public ResponseEntity<?> addReply(@RequestHeader("Authorization") String token, @RequestBody ExhibitionCustomerReplyDto exhibitionCustomerReplyDto) {
        try {
            List<ExhibitionCommentDTO> exhibitionCommentDTOList = exhibitionService.addReply(getMyUuId(token), exhibitionCustomerReplyDto.getBoardId(), exhibitionCustomerReplyDto.getComment(), exhibitionCustomerReplyDto.getCommentGroupId(), exhibitionCustomerReplyDto.getSequence());
            return getResponseMessage(StatusEnum.OK, "대댓글 삽입 성공 업데이트 대댓글 목록", exhibitionCommentDTOList);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            return getResponseMessage(StatusEnum.BAD_REQUEST, dataIntegrityViolationException.getMessage());
        }
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token, String commentId){
        try{
            exhibitionService.deleteComment(getMyUuId(token), commentId);
            return getResponseMessage(StatusEnum.OK, "댓글 삭제 성공", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<?> updateComment(@RequestHeader("Authorization") String token, @RequestBody ExhibitionCommentAlterDTO exhibitionCommentAlterDTO){
        try{
            ExhibitionCommentDTO exhibitionCommentDTO = exhibitionService.alterComment(getMyUuId(token), exhibitionCommentAlterDTO);
            return getResponseMessage(StatusEnum.OK, "댓글 수정 성공", exhibitionCommentDTO);
        }catch (NullPointerException nullPointerException){
            nullPointerException.getMessage();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
